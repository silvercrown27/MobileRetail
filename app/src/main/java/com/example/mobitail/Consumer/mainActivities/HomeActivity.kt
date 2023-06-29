package com.example.mobitail.Consumer.mainActivities

import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

import com.example.mobitail.R
import com.example.mobitail.Consumer.adapterClasses.HomeItemsAdapter
import com.example.mobitail.Consumer.adapterClasses.HomeAdapter
import com.example.mobitail.Consumer.adapterClasses.SliderAdapter
import com.example.mobitail.Consumer.modalClasses.SliderData
import com.example.mobitail.Consumer.secondaryActivities.CerialsActivity
import com.example.mobitail.Consumer.secondaryActivities.ClothingActivity
import com.example.mobitail.Consumer.secondaryActivities.DrinksActivity
import com.example.mobitail.Consumer.secondaryActivities.ElectronicsActivity
import com.example.mobitail.Consumer.secondaryActivities.GroceriesActivity
import com.example.mobitail.Consumer.secondaryActivities.SnacksActivity
import com.example.mobitail.Consumer.secondaryActivities.StationariesActivity
import com.example.mobitail.Consumer.secondaryActivities.SweetsActivity
import com.smarteist.autoimageslider.SliderView

class HomeActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var customSliderAdapter: SliderAdapter
    private lateinit var HomeRecycleAdapter: HomeAdapter
    private lateinit var groupsRV: RecyclerView
    private lateinit var slider: SliderView
    private lateinit var homeItemsAdapter: HomeItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        slider = findViewById(R.id.slider)
        customSliderAdapter = SliderAdapter()
        slider.setSliderAdapter(customSliderAdapter)
        slider.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        slider.setScrollTimeInSec(3);
        slider.setAutoCycle(true);
        slider.startAutoCycle();

        groupsRV = findViewById(R.id.home_items)
        homeItemsAdapter = HomeItemsAdapter()
        groupsRV.adapter = homeItemsAdapter
        groupsRV.layoutManager = GridLayoutManager(this, 2)
        groupsRV.addItemDecoration(SpacingItemDecoration(10))


        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.action_home
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_categories -> {
                    val intent = Intent(this, CategoriesActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)
                    true
                }
                R.id.action_cart -> {
                    val intent = Intent(this, CartActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)
                    true
                }
                R.id.action_home -> {
                    true
                }
                R.id.action_favorites -> {
                    val intent = Intent(this, FavoritesActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)
                    true
                }
                R.id.action_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)
                    true
                }
                else -> false
            }
        }
        populateGroupList()
        populatesliderList()
    }

    private fun populateItemList() {
        val itemList = ArrayList<HomeAdapter.Item>()

        val item1 = HomeAdapter.Item(R.drawable.a, "Groceries", GroceriesActivity::class.java)
        val item2 = HomeAdapter.Item(R.drawable.b, "Electronics", ElectronicsActivity::class.java)
        val item3 = HomeAdapter.Item(R.drawable.c, "Clothing", ClothingActivity::class.java)
        val item4 = HomeAdapter.Item(R.drawable.d, "Stationary", StationariesActivity::class.java)
        val item5 = HomeAdapter.Item(R.drawable.e, "Cerials", CerialsActivity::class.java)
        val item6 = HomeAdapter.Item(R.drawable.f, "Snacks", SnacksActivity::class.java)
        val item7 = HomeAdapter.Item(R.drawable.g, "Sweets", SweetsActivity::class.java)
        val item8 = HomeAdapter.Item(R.drawable.g, "Drinks", DrinksActivity::class.java)

        itemList.add(item1)
        itemList.add(item2)
        itemList.add(item3)
        itemList.add(item4)
        itemList.add(item5)
        itemList.add(item6)
        itemList.add(item7)
        itemList.add(item8)

        HomeRecycleAdapter.setItemList(itemList)
    }

    private fun populatesliderList() {
        val sliderList = ArrayList<SliderData>()

        val item1 = SliderData(R.drawable.a)
        val item2 = SliderData(R.drawable.b)
        val item3 = SliderData(R.drawable.c)

        sliderList.add(item1)
        sliderList.add(item2)
        sliderList.add(item3)

        customSliderAdapter.setItemList(sliderList)
    }

    private fun populateGroupList() {
        val groupList = ArrayList<HomeItemsAdapter.HomeItemGroups>()

        val item1 = HomeItemsAdapter.HomeItemGroups("Kales", R.drawable.b)
        val item2 = HomeItemsAdapter.HomeItemGroups("Oranges", R.drawable.d)
        val item3 = HomeItemsAdapter.HomeItemGroups("Mangoes", R.drawable.g)
        val item4 = HomeItemsAdapter.HomeItemGroups("Milk", R.drawable.a)
        val item5 = HomeItemsAdapter.HomeItemGroups("WaterMellon", R.drawable.f)
        val item6 = HomeItemsAdapter.HomeItemGroups("Coconuts", R.drawable.e)
        val item7 = HomeItemsAdapter.HomeItemGroups("Cabbages", R.drawable.d)
        val item8 = HomeItemsAdapter.HomeItemGroups("Onions", R.drawable.g)

        groupList.add(item1)
        groupList.add(item2)
        groupList.add(item3)
        groupList.add(item4)
        groupList.add(item5)
        groupList.add(item6)
        groupList.add(item7)
        groupList.add(item8)

        homeItemsAdapter.setItemList(groupList)
    }
}
class SpacingItemDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.right = spacing
    }
}
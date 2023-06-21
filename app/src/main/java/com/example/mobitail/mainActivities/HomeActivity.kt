package com.example.mobitail.mainActivities

import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.helper.widget.Carousel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

import com.example.mobitail.R
import com.example.mobitail.adapters.CustomAdapter
import com.example.mobitail.secondaryActivities.CerialsActivity
import com.example.mobitail.secondaryActivities.ClothingActivity
import com.example.mobitail.secondaryActivities.DrinksActivity
import com.example.mobitail.secondaryActivities.ElectronicsActivity
import com.example.mobitail.secondaryActivities.GroceriesActivity
import com.example.mobitail.secondaryActivities.SnacksActivity
import com.example.mobitail.secondaryActivities.StationariesActivity
import com.example.mobitail.secondaryActivities.SweetsActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var customAdapter: CustomAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

//        val carousel: Carousel = findViewById(R.id.carousell)
//        val itemList = listOf(
//            CarouselAdapter.Item(R.drawable.b),
//            CarouselAdapter.Item(R.drawable.profile),
//            // Add more items as needed
//        )
//        val adapter = CarouselAdapter(this, itemList)
//        carousel.setAdapter(adapter)

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

        recyclerView = findViewById(R.id.RecycleView1)
        customAdapter = CustomAdapter()
        recyclerView.adapter = customAdapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.addItemDecoration(SpacingItemDecoration(10))

        populateItemList()
    }

    private fun populateItemList() {
        val itemList = ArrayList<CustomAdapter.Item>()

        val item1 = CustomAdapter.Item(R.drawable.a, "Groceries", GroceriesActivity::class.java)
        val item2 = CustomAdapter.Item(R.drawable.b, "Electronics", ElectronicsActivity::class.java)
        val item3 = CustomAdapter.Item(R.drawable.c, "Clothing", ClothingActivity::class.java)
        val item4 = CustomAdapter.Item(R.drawable.d, "Stationary", StationariesActivity::class.java)
        val item5 = CustomAdapter.Item(R.drawable.e, "Cerials", CerialsActivity::class.java)
        val item6 = CustomAdapter.Item(R.drawable.f, "Snacks", SnacksActivity::class.java)
        val item7 = CustomAdapter.Item(R.drawable.g, "Sweets", SweetsActivity::class.java)
        val item8 = CustomAdapter.Item(R.drawable.g, "Drinks", DrinksActivity::class.java)

        itemList.add(item1)
        itemList.add(item2)
        itemList.add(item3)
        itemList.add(item4)
        itemList.add(item5)
        itemList.add(item6)
        itemList.add(item7)

        customAdapter.setItemList(itemList)
    }
}
class SpacingItemDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.right = spacing
    }
}
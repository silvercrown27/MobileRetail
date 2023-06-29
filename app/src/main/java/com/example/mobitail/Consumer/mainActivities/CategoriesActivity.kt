package com.example.mobitail.Consumer.mainActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobitail.R
import com.example.mobitail.Consumer.adapterClasses.CategoriesAdapter
import com.example.mobitail.Consumer.adapterClasses.CategoryItemsAdapter
import com.example.mobitail.Consumer.secondaryActivities.CerialsActivity
import com.example.mobitail.Consumer.secondaryActivities.ClothingActivity
import com.example.mobitail.Consumer.secondaryActivities.DrinksActivity
import com.example.mobitail.Consumer.secondaryActivities.ElectronicsActivity
import com.example.mobitail.Consumer.secondaryActivities.GroceriesActivity
import com.example.mobitail.Consumer.secondaryActivities.SnacksActivity
import com.example.mobitail.Consumer.secondaryActivities.StationariesActivity
import com.example.mobitail.Consumer.secondaryActivities.SweetsActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class CategoriesActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var customAdapter: CategoriesAdapter
    private lateinit var categoryitemsRV: RecyclerView
    private lateinit var categoryItemsAdapter: CategoryItemsAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = 0
        bottomNavigationView.selectedItemId = R.id.action_categories
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_categories -> {
                    // Start CategoriesActivity
                    val intent = Intent(this, CategoriesActivity::class.java)
                    startActivity(intent)

                    overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)
                    return@setOnItemSelectedListener true
                }
                R.id.action_cart -> {
                    val intent = Intent(this, CartActivity::class.java)
                    startActivity(intent)

                    overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)
                    return@setOnItemSelectedListener true
                }
                R.id.action_home -> {
                    // Start HomeActivity
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)

                    overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)
                    return@setOnItemSelectedListener true
                }
                R.id.action_favorites -> {
                    // Start FavoritesActivity
                    val intent = Intent(this, FavoritesActivity::class.java)
                    startActivity(intent)

                    overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)
                    return@setOnItemSelectedListener true
                }
                R.id.action_profile -> {
                    // Start ProfileActivity
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)

                    overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)
                    return@setOnItemSelectedListener true
                }

                else -> {
                    return@setOnItemSelectedListener true
                }
            }
        }
        recyclerView = findViewById(R.id.category_options)
        customAdapter = CategoriesAdapter()
        recyclerView.adapter = customAdapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.addItemDecoration(SpacingItemDecoration(10))

        categoryitemsRV = findViewById(R.id.category_groups)
        categoryItemsAdapter = CategoryItemsAdapter()
        categoryitemsRV.adapter = categoryItemsAdapter
        categoryitemsRV.layoutManager = LinearLayoutManager(this)

        populateItemList()
        populateGroupList()

    }
    private fun populateItemList() {
        val itemList = ArrayList<CategoriesAdapter.Item>()

        val item1 = CategoriesAdapter.Item("Groceries", GroceriesActivity::class.java)
        val item2 = CategoriesAdapter.Item("Electronics", ElectronicsActivity::class.java)
        val item3 = CategoriesAdapter.Item("Clothing", ClothingActivity::class.java)
        val item4 = CategoriesAdapter.Item("Stationary", StationariesActivity::class.java)
        val item5 = CategoriesAdapter.Item("Cerials", CerialsActivity::class.java)
        val item6 = CategoriesAdapter.Item("Snacks", SnacksActivity::class.java)
        val item7 = CategoriesAdapter.Item("Sweets", SweetsActivity::class.java)
        val item8 = CategoriesAdapter.Item("Drinks", DrinksActivity::class.java)

        itemList.add(item1)
        itemList.add(item2)
        itemList.add(item3)
        itemList.add(item4)
        itemList.add(item5)
        itemList.add(item6)
        itemList.add(item7)
        itemList.add(item8)

        customAdapter.setItemList(itemList)
    }

    private fun populateGroupList() {
        val groupList = ArrayList<CategoryItemsAdapter.CategoryGroups>()

        val item1 = CategoryItemsAdapter.CategoryGroups(
            R.drawable.b,
            "Kales",
            "Fresh and nutritious kales",
            "Ksh.",
            "50.00"
        )
        val item2 = CategoryItemsAdapter.CategoryGroups(
            R.drawable.d,
            "Oranges",
            "Juicy and tangy oranges",
            "Ksh.",
            "80.00"
        )
        val item3 = CategoryItemsAdapter.CategoryGroups(
            R.drawable.g,
            "Mangoes",
            "Sweet and ripe mangoes",
            "Ksh.",
            "120.00"
        )
        val item4 = CategoryItemsAdapter.CategoryGroups(
            R.drawable.a,
            "Milk",
            "Fresh and creamy milk",
            "Ksh.",
            "70.00"
        )
        val item5 = CategoryItemsAdapter.CategoryGroups(
            R.drawable.f,
            "Watermelon",
            "Refreshing watermelon",
            "Ksh.",
            "90.00"
        )
        val item6 = CategoryItemsAdapter.CategoryGroups(
            R.drawable.e,
            "Coconuts",
            "Naturally delicious coconuts",
            "Ksh.",
            "40.00"
        )
        val item7 = CategoryItemsAdapter.CategoryGroups(
            R.drawable.d,
            "Cabbages",
            "Crisp and green cabbages",
            "Ksh.",
            "60.00"
        )
        val item8 = CategoryItemsAdapter.CategoryGroups(
            R.drawable.g,
            "Onions",
            "Fresh and aromatic onions",
            "Ksh.",
            "35.00"
        )

        groupList.add(item1)
        groupList.add(item2)
        groupList.add(item3)
        groupList.add(item4)
        groupList.add(item5)
        groupList.add(item6)
        groupList.add(item7)
        groupList.add(item8)

        categoryItemsAdapter.setItemList(groupList)
    }

    data class CategoryGroups(
        val item_image: Int,
        val item_name: String,
        val item_desc: String,
        val item_currency: String,
        val item_price: String
    )

}
package com.example.mobitail.mainActivities

import android.content.Intent
import android.media.session.PlaybackState.CustomAction
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobitail.R
import com.example.mobitail.adapters.CategoriesAdapter
import com.example.mobitail.adapters.HomeAdapter
import com.example.mobitail.secondaryActivities.CerialsActivity
import com.example.mobitail.secondaryActivities.ClothingActivity
import com.example.mobitail.secondaryActivities.DrinksActivity
import com.example.mobitail.secondaryActivities.ElectronicsActivity
import com.example.mobitail.secondaryActivities.GroceriesActivity
import com.example.mobitail.secondaryActivities.SnacksActivity
import com.example.mobitail.secondaryActivities.StationariesActivity
import com.example.mobitail.secondaryActivities.SweetsActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class CategoriesActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var customAdapter: CategoriesAdapter
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

        populateItemList()
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
}
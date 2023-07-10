package com.example.mobitail.consumer.mainActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobitail.R
import com.example.mobitail.consumer.adapterClasses.CartAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

class CartActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var cartItems: RecyclerView
    private lateinit var cartItems_Adapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        cartItems = findViewById(R.id.cart_items)
        cartItems_Adapter = CartAdapter()
        cartItems.adapter = cartItems_Adapter
        cartItems.layoutManager = LinearLayoutManager(this)

        populateCartItems()

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = 0
        bottomNavigationView.selectedItemId = R.id.action_cart
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
    }
    private fun populateCartItems() {
        val items = ArrayList<CartAdapter.CartItems>()

        val item1 = CartAdapter.CartItems(
            R.drawable.d,
            "Item 1",
            "Description for Item 1",
            "Ksh.",
            "100.00"
        )
        val item2 = CartAdapter.CartItems(
            R.drawable.e,
            "Item 2",
            "Description for Item 2",
            "Ksh.",
            "150.00"
        )
        val item3 = CartAdapter.CartItems(
            R.drawable.f,
            "Item 3",
            "Description for Item 3",
            "Ksh.",
            "200.00"
        )
        val item4 = CartAdapter.CartItems(
            R.drawable.a,
            "Item 4",
            "Description for Item 4",
            "Ksh.",
            "120.00"
        )
        val item5 = CartAdapter.CartItems(
            R.drawable.b,
            "Item 5",
            "Description for Item 5",
            "Ksh.",
            "180.00"
        )
        val item6 = CartAdapter.CartItems(
            R.drawable.a,
            "Item 6",
            "Description for Item 6",
            "Ksh.",
            "90.00"
        )
        val item7 = CartAdapter.CartItems(
            R.drawable.g,
            "Item 7",
            "Description for Item 7",
            "Ksh.",
            "220.00"
        )

        items.add(item1)
        items.add(item2)
        items.add(item3)
        items.add(item4)
        items.add(item5)
        items.add(item6)
        items.add(item7)

        cartItems_Adapter.setItemList(items)
    }
}
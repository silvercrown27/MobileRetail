package com.example.mobitail.Consumer.mainActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mobitail.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class FavoritesActivity : AppCompatActivity() {
    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = 0
        bottomNavigationView.selectedItemId = R.id.action_favorites
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
}
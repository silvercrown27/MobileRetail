package com.example.mobitail.consumer.mainActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

import com.example.mobitail.FirebaseUtils
import com.example.mobitail.consumer.adapterClasses.ProfileRecycleView
import com.example.mobitail.consumer.modalClasses.ProfileItems
import com.example.mobitail.R

import com.google.android.material.bottomnavigation.BottomNavigationView

import de.hdodenhof.circleimageview.CircleImageView

class ProfileActivity : AppCompatActivity() {
    private lateinit var profileImage: CircleImageView
    private lateinit var user_firstname: TextView
    private lateinit var user_name: TextView
    private lateinit var user_email: TextView
    private lateinit var user_location: TextView
    private lateinit var user_gender: TextView

    private lateinit var profile_rv: RecyclerView
    private lateinit var profile_rv_adapter: ProfileRecycleView
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var settings_btn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profileImage = findViewById(R.id.Profile_picture)
        profile_rv = findViewById(R.id.profile_RV)
        settings_btn = findViewById(R.id.Setting_btn)
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        user_firstname = findViewById(R.id.user_firstname)
        user_name = findViewById(R.id.user_name)
        user_email = findViewById(R.id.user_email)
        user_location = findViewById(R.id.user_location)
        user_gender = findViewById(R.id.User_gender)

        FirebaseUtils.prepareData(this@ProfileActivity) { user ->
            if (user != null) {
                val email = user.email
                val name = user.firstname + " " + user.lastname
                val location = user.location
                val gender = user.gender
                val profileImg = user.profileimage

                user_name.text = name
                user_email.text = email
                user_location.text = location
                user_gender.text = gender
                user_firstname.text = user.firstname

                Glide.with(this@ProfileActivity)
                    .load(profileImg)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(profileImage)

            } else {
                Toast.makeText(this@ProfileActivity, "User data is not available", Toast.LENGTH_SHORT).show()
            }
        }

        profile_rv_adapter = ProfileRecycleView()
        profile_rv.layoutManager = GridLayoutManager(this, 2)
        profile_rv.adapter = profile_rv_adapter
        profile_rv.addItemDecoration(SpacingItemDecoration(18))

        settings_btn.setOnClickListener {
            var intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        bottomNavigationView.selectedItemId = 0
        bottomNavigationView.selectedItemId = R.id.action_profile
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
        populateProfileList()
    }
    private fun populateProfileList() {
        val groupList = ArrayList<ProfileItems>()

        val item1 = ProfileItems(R.drawable.wallets,
            "Payment Options",
            "Choose and selct your desired payment option",
            HomeActivity::class.java)

        val item2 = ProfileItems(R.drawable.messages,
            "Message",
            "Check and connect with desired shops",
            SettingsActivity::class.java)

        val item3 = ProfileItems(R.drawable.purchases,
            "Purchases",
            "Check your order and payment history",
            FavoritesActivity::class.java)

        val item4 = ProfileItems(R.drawable.address,
            "Addresses",
            "Choose your desired location for an easier time",
            CartActivity::class.java)

        groupList.add(item1)
        groupList.add(item2)
        groupList.add(item3)
        groupList.add(item4)

        profile_rv_adapter.setItemList(groupList)
    }
}
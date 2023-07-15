package com.example.mobitail.consumer.mainActivities

import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.mobitail.FirebaseUtils
import com.google.android.material.bottomnavigation.BottomNavigationView

import com.example.mobitail.R
import com.example.mobitail.consumer.adapterClasses.HomeItemsAdapter
import com.example.mobitail.consumer.adapterClasses.SliderAdapter
import com.example.mobitail.consumer.modalClasses.SliderData
import com.example.mobitail.databaseorganization.Products
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.smarteist.autoimageslider.SliderView
import de.hdodenhof.circleimageview.CircleImageView

class HomeActivity : AppCompatActivity() {
    private lateinit var profileImg: CircleImageView
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var customSliderAdapter: SliderAdapter
    private lateinit var groupsRV: RecyclerView
    private lateinit var slider: SliderView
    private lateinit var homeItemsAdapter: HomeItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        slider = findViewById(R.id.slider)
        profileImg = findViewById(R.id.Profile_picture)

        FirebaseUtils.prepareData(this@HomeActivity) { user ->
            if (user != null) {
                Glide.with(this@HomeActivity)
                    .load(user.profileimage)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(profileImg)

            } else {
                Toast.makeText(this@HomeActivity, "User data is not available", Toast.LENGTH_SHORT).show()
            }
        }

        customSliderAdapter = SliderAdapter()
        slider.setSliderAdapter(customSliderAdapter)
        slider.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        slider.setScrollTimeInSec(3);
        slider.setAutoCycle(true);
        slider.startAutoCycle();

        homeItemsAdapter = HomeItemsAdapter()
        homeItemsAdapter.setOnItemClickListener(object : HomeItemsAdapter.OnItemClickListener {
            override fun onItemClick(item: Products) {
                val intent = Intent(this@HomeActivity, ProductDetails::class.java)
                intent.putExtra("item_label", "$item.prodName\n${item.prodBrand}")
                intent.putExtra("item_description", item.prodDescription)
                intent.putExtra("item_image", item.prodImage)
                startActivity(intent)
            }
        })

        groupsRV = findViewById(R.id.home_items)
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
        val groupList = ArrayList<Products>()
        var currentUser = FirebaseAuth.getInstance().currentUser!!.uid
        val prodDbRef = FirebaseDatabase.getInstance().getReference("products")

        prodDbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                groupList.clear()

                for (productSnapshot in snapshot.children) {
                    val product = productSnapshot.getValue(Products::class.java)
                    product?.let {
                        groupList.add(it)
                    }

                }
                homeItemsAdapter.setItemList(groupList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if needed
                Toast.makeText(
                    this@HomeActivity,
                    "An error occured while accessing the database",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }
}

class SpacingItemDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.right = spacing
    }
}
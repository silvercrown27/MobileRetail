package com.example.mobitail.retailer.mainActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobitail.consumer.adapterClasses.HomeItemsAdapter
import com.example.mobitail.consumer.mainActivities.SpacingItemDecoration
import com.example.mobitail.R
import com.example.mobitail.databaseorganization.Products
import com.google.android.material.button.MaterialButton

class FinalSignupstep : AppCompatActivity() {
    private lateinit var groupsRV: RecyclerView
    private lateinit var ItemsAdapter: HomeItemsAdapter
    private lateinit var create_ac_btn: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final_signupstep)

        create_ac_btn = findViewById(R.id.create_ac_btn)
        groupsRV = findViewById(R.id.items)

        ItemsAdapter = HomeItemsAdapter()
        groupsRV.adapter = ItemsAdapter
        groupsRV.layoutManager = LinearLayoutManager(this)
        groupsRV.addItemDecoration(SpacingItemDecoration(10))

        create_ac_btn.setOnClickListener {
            var intent = Intent(this, RetailDashboardActivity::class.java)
            startActivity(intent)
        }
        populateGroupLists()
    }

    private fun populateGroupLists() {
        val groupList = ArrayList<Products>()

        ItemsAdapter.setItemList(groupList)
    }
}
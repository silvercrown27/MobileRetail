package com.example.mobitail.retailer.mainActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobitail.consumer.adapterClasses.HomeItemsAdapter
import com.example.mobitail.consumer.mainActivities.SpacingItemDecoration
import com.example.mobitail.R
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

        ItemsAdapter.setItemList(groupList)
    }
}
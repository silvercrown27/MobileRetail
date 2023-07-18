package com.example.mobitail.retailer.mainActivities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.mobitail.consumer.mainActivities.SpacingItemDecoration
import com.example.mobitail.R
import com.example.mobitail.databaseorganization.Categories
import com.example.mobitail.retailer.adapterClasses.SelectCategoryAdapter
import com.example.mobitail.retailer.adapterClasses.SelectedCatAdapter

import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class FinalSignupStep : AppCompatActivity() {
    private lateinit var StoreCategoriesRV: RecyclerView
    private lateinit var StoreCategoriesAdapter: SelectCategoryAdapter
    private lateinit var selectedRV: RecyclerView
    private lateinit var selectedCategorieasAdapter: SelectedCatAdapter
    private lateinit var create_ac_btn: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final_signupstep)

        create_ac_btn = findViewById(R.id.create_ac_btn)
        StoreCategoriesRV = findViewById(R.id.categories_groups)
        selectedRV = findViewById(R.id.selected_categories)

        StoreCategoriesAdapter = SelectCategoryAdapter()
        StoreCategoriesAdapter.setOnItemClickListener(object : SelectCategoryAdapter.OnItemClickListener {
            override fun onItemClick(item: Categories) {
                onCategorySelected(item)
            }
        })
        StoreCategoriesRV.adapter = StoreCategoriesAdapter
        StoreCategoriesRV.layoutManager = GridLayoutManager(this, 2)
        StoreCategoriesRV.addItemDecoration(SpacingItemDecoration(10))

        selectedCategorieasAdapter = SelectedCatAdapter()
        selectedRV.adapter = selectedCategorieasAdapter
        selectedRV.layoutManager = LinearLayoutManager(this)

        create_ac_btn.setOnClickListener {
            val selectedCategories = StoreCategoriesAdapter.getSelectedCategories()

            val selectedCategoriesText = selectedCategories.joinToString(", ")
            Toast.makeText(this, "Selected Categories: $selectedCategoriesText", Toast.LENGTH_SHORT).show()

            setType(selectedCategoriesText)
        }

        populateGroupLists()
    }

    private fun populateGroupLists() {
        val groupList = ArrayList<Categories>()
        val catdb = FirebaseDatabase.getInstance().getReference("categories")

        catdb.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                groupList.clear()

                for (productSnapshot in snapshot.children) {
                    val product = productSnapshot.getValue(Categories::class.java)
                    product?.let {
                        groupList.add(it)
                    }
                }
                StoreCategoriesAdapter.setItemList(groupList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if needed
                Toast.makeText(
                    this@FinalSignupStep,
                    "An error occurred while accessing the database",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun onCategorySelected(selectedCategory: Categories) {
        val label = selectedCategory.cat_Name
        if (label != null) {
            selectedCategorieasAdapter.addSelectedCategory(label)
        }
    }

    private fun setType(selectedCategoriesText: String) {
        val storedb = FirebaseDatabase.getInstance().getReference("stores")
        val key = intent.getStringExtra("storeid")

        if (key != null) {
            storedb.orderByKey().equalTo(key).addListenerForSingleValueEvent(object : ValueEventListener {
                @SuppressLint("HardwareIds")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {
                            userSnapshot.child("storetype").ref.setValue(selectedCategoriesText)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(
                                            this@FinalSignupStep,
                                            "Store Added Successfully!",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        val intent = Intent(
                                            this@FinalSignupStep,
                                            RetailDashboardActivity::class.java
                                        )
                                        startActivity(intent)
                                        overridePendingTransition(
                                            R.anim.fade_animation,
                                            R.anim.fade_out
                                        )
                                    } else {
                                        Toast.makeText(
                                            this@FinalSignupStep,
                                            "Error: ${task.exception?.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        }
                    } else {
                        // No store with the specified key found
                        Toast.makeText(
                            this@FinalSignupStep,
                            "Account does not exist",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                    Toast.makeText(
                        this@FinalSignupStep,
                        "An error occurred while accessing the database.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

}
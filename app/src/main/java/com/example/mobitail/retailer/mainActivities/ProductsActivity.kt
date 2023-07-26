package com.example.mobitail.retailer.mainActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobitail.R
import com.example.mobitail.consumer.adapterClasses.HomeItemsAdapter
import com.example.mobitail.consumer.mainActivities.ProductDetails
import com.example.mobitail.databaseorganization.Products
import com.example.mobitail.retailer.adapterClasses.StoreProductsAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProductsActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var prodDbRef: DatabaseReference
    private lateinit var addProduct: MaterialButton
    private lateinit var rmProduct: MaterialButton
    private lateinit var productsAdapter: StoreProductsAdapter
    private lateinit var productListRV: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)

        addProduct = findViewById(R.id.addProduct_btn)
        productListRV = findViewById(R.id.product_list_rv)
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.action_products

        productsAdapter = StoreProductsAdapter()
        onProductClick(productsAdapter)
        productListRV.adapter = productsAdapter
        productListRV.layoutManager = LinearLayoutManager(this)

        addProduct.setOnClickListener {
            val intent = Intent(this, AddRemoveProduct::class.java)
            startActivity(intent)
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_accounts -> {
                    val intent = Intent(this, RetailAccountsActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)
                    true
                }
                R.id.action_dashboard -> {
                    val intent = Intent(this, RetailDashboardActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)
                    true
                }
                R.id.action_products -> {
                    val intent = Intent(this, ProductsActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)
                    true
                }
                R.id.action_settings -> {
                    val intent = Intent(this, RetailSettingsActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_animation, R.anim.fade_out)
                    true
                }
                else -> false
            }
        }
        populateGroupList()
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
                    if (product?.userid == currentUser){
                        product?.let {
                            groupList.add(it)
                        }
                    }

                }

                productsAdapter.setItemList(groupList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if needed
                Toast.makeText(
                    this@ProductsActivity,
                    "An error occured while accessing the database",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun onProductClick(adapter: StoreProductsAdapter) {
        adapter.setOnItemClickListener(object : StoreProductsAdapter.OnItemClickListener {
            override fun onItemClick(item: Products) {
                val intent = Intent(this@ProductsActivity, StoreProductsActivity::class.java)
                intent.putExtra("product", item)
                startActivity(intent)
            }
        })
    }
}

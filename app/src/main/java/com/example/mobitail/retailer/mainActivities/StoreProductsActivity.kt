package com.example.mobitail.retailer.mainActivities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.mobitail.R
import com.example.mobitail.databaseorganization.Products

class StoreProductsActivity : AppCompatActivity() {

    private lateinit var prod_image: ImageView
    private lateinit var prod_label: TextView
    private lateinit var prod_description: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store_products)

        prod_label = findViewById(R.id.prod_label)
        prod_image = findViewById(R.id.prod_image)
        prod_description = findViewById(R.id.prod_description)

        val product: Products? = intent.getSerializableExtra("product") as? Products
        product?.let { (prodName, prodImage, prodBrand, prodDescription, prodPrice, quantity, sold, storeId, likes, reviews, dateAdded) ->

            prod_label.text = prodBrand
            prod_description.text = prodDescription
            Glide.with(this)
                .load(prodImage)
                .into(prod_image)
        }
    }
}
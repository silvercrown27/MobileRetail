package com.example.mobitail.consumer.mainActivities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.mobitail.R

class ProductDetails : AppCompatActivity() {
    private lateinit var prod_image: ImageView
    private lateinit var prod_label: TextView
    private lateinit var prod_description: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detals)

        prod_label = findViewById(R.id.prod_label)
        prod_image = findViewById(R.id.prod_image)
        prod_description = findViewById(R.id.prod_description)

        val label = intent.getStringExtra("item_label")
        val description = intent.getStringExtra("item_description")
        val imageResId = intent.getIntExtra("item_image", R.drawable.categories)

        prod_label.text = label
        prod_description.text = description
        prod_image.setImageResource(imageResId)
    }
}

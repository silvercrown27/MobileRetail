package com.example.mobitail.consumer.mainActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.mobitail.PaymentOptionsActivity
import com.example.mobitail.R
import com.example.mobitail.databaseorganization.CartItems
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.w3c.dom.Text

class ActivityCheckout : AppCompatActivity() {
    private lateinit var totalItems: TextView
    private lateinit var subtotal: TextView
    private lateinit var shoppingCost: TextView
    private lateinit var totalCost: TextView
    private lateinit var completeCheckout: MaterialButton
    private lateinit var cdbref: DatabaseReference
    private lateinit var cartid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        totalItems = findViewById(R.id.total_items)
        subtotal = findViewById(R.id.subtotal)
        shoppingCost = findViewById(R.id.shopping_cost)
        totalCost = findViewById(R.id.totalCost)
        completeCheckout = findViewById(R.id.checkoutBtn)

        checkdb(totalItems, subtotal, shoppingCost, totalCost)
    }

    private fun checkdb(
        totalItems: TextView,
        subtotal: TextView,
        shoppingcost: TextView,
        totalCost: TextView
    ) {
        val shopcosts = 0
        cartid = intent.getStringExtra("cartid").toString()
        cdbref = FirebaseDatabase.getInstance().getReference("cartitems")

        cdbref.orderByChild("cartId").equalTo(cartid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var totalProductCount = 0
                var totalPriceSum = 0.0

                for (cartSnapshot in snapshot.children) {
                    val cartItem = cartSnapshot.getValue(CartItems::class.java)
                    cartItem?.let {
                        totalProductCount += cartItem.quantity
                        totalPriceSum += it.prodPrice * it.quantity
                    }
                }

                totalItems.text = totalProductCount.toString()
                subtotal.text = totalPriceSum.toString()
                shoppingcost.text = shopcosts.toString()
                totalCost.text = (shopcosts + totalPriceSum).toString()
                completeCheckout = findViewById(R.id.checkoutBtn)
                completeCheckout.setOnClickListener {
                    val intent = Intent(this@ActivityCheckout, PaymentOptionsActivity::class.java)
                    intent.putExtra("amount", (shopcosts + totalPriceSum).toInt())
                    startActivity(intent)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled if needed
            }
        })

    }
}

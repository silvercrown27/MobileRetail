package com.example.mobitail.consumer.mainActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.mobitail.FirebaseUtils
import com.example.mobitail.R
import com.example.mobitail.consumer.adapterClasses.CartAdapter
import com.example.mobitail.databaseorganization.CartItems
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView

class CartActivity : AppCompatActivity() {
    private lateinit var profileImg: CircleImageView
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var cartItems: RecyclerView
    private lateinit var cartItems_Adapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        cartItems = findViewById(R.id.cart_items)
        profileImg = findViewById(R.id.Profile_picture)

        FirebaseUtils.prepareData(this) { user ->
            if (user != null) {
                Glide.with(this)
                    .load(user.profileimage)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(profileImg)

            } else {
                Toast.makeText(this, "User data is not available", Toast.LENGTH_SHORT).show()
            }
        }
        cartItems_Adapter = CartAdapter()
        cartItems.adapter = cartItems_Adapter
        cartItems.layoutManager = LinearLayoutManager(this)

        populateCartItems()

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = 0
        bottomNavigationView.selectedItemId = R.id.action_cart
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

    private fun populateCartItems() {
        val cart = ArrayList<CartItems>()
        val cartitemsDbRef = FirebaseDatabase.getInstance().getReference("cartitems")

        cartitemsDbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                cart.clear()

                for (productSnapshot in snapshot.children) {
                    val product = productSnapshot.getValue(CartItems::class.java)

                    getCart(FirebaseAuth.getInstance().currentUser!!.uid) { cartId ->
                        if (cartId != null && product!!.cartId == cartId) {
                            cartItems_Adapter.setItemList(cart)
                        } else {
                            showToast("Your Cart is empty")
                        }
                    }
                    product?.let {
                        cart.add(it)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                showToast("An error occured while accessing the database")
            }
        })
    }

    private fun getCart(userid: String, cartIdCallback: (String?) -> Unit) {
        val cartdb = FirebaseDatabase.getInstance().getReference("carts")

        cartdb.orderByChild("userid").equalTo(userid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (cartSnapshot in dataSnapshot.children) {
                        val cartid = cartSnapshot.key
                        if (cartid != null) {
                            cartIdCallback(cartid)
                            return
                        }
                    }
                    showToast("Your Cart is empty")
                    cartIdCallback(null)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    showToast("Failed to retrieve cart data: ${databaseError.message}")
                    cartIdCallback(null)
                }
            })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
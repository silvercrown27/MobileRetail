package com.example.mobitail.consumer.adapterClasses

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

import com.example.mobitail.R
import com.example.mobitail.databaseorganization.Cart
import com.example.mobitail.databaseorganization.CartItems
import com.example.mobitail.databaseorganization.Products
import com.example.mobitail.databaseorganization.Secrets

import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue


class HomeItemsAdapter(private val context: Context)
    : RecyclerView.Adapter<HomeItemsAdapter.ViewHolder>() {
    private val groupList: ArrayList<Products> = ArrayList()
    private var itemClickListener: OnItemClickListener? = null

    fun setItemList(items: ArrayList<Products>) {
        groupList.clear()
        groupList.addAll(items)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_screen_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = groupList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return groupList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardView: MaterialCardView = itemView.findViewById(R.id.item_properties)
        private val addToCart: ImageView = itemView.findViewById(R.id.add_to_cart_btn)
        private val imageView: ImageView = itemView.findViewById(R.id.groupImage)
        private val textView: TextView = itemView.findViewById(R.id.groupName)
        private val itemlabel: TextView = itemView.findViewById(R.id.prod_label)
        private val currentUser: String = FirebaseAuth.getInstance().currentUser!!.uid

        init {
            cardView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = groupList[position]
                    itemClickListener?.onItemClick(item)
                }
            }
        }

        fun bind(item: Products) {
            val context: Context = itemView.context
            val cartdb = FirebaseDatabase.getInstance().getReference("carts")

            textView.text = item.prodBrand
            itemlabel.text = item.prodName
            Glide.with(context)
                .load(item.prodImage)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView)

            addToCart.setOnClickListener {
                getProductId(item) {productId ->
                    isCartExists(currentUser) { cartid ->
                        if (cartid != "empty" && cartid != "e")
                            createCartItem(item, cartid, productId)
                        else
                        {
                            val new_cartId = cartdb.push().key
                            val cart = Cart(userid = currentUser)

                            if (new_cartId != null) {
                                cartdb.child(new_cartId).setValue(cart).addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        showToast("Cart added successfully!")
                                        createCartItem(item, new_cartId, productId)
                                    } else
                                        showToast("Failed to add cart: ${task.exception?.message}")
                                }
                            } else showToast("Failed to generate cart key.")
                        }
                    }
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: Products)
    }

    private fun isCartExists(userid: String, cartExistsCallback: (String) -> Unit) {
        val cartdb = FirebaseDatabase.getInstance().getReference("carts")

        val query = cartdb.orderByChild("userid").equalTo(userid)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val cartSnapshot = dataSnapshot.children.first()
                    val cartId = cartSnapshot.key

                    if (cartId != null)
                    {
                        cartExistsCallback(cartId)
                        return
                    }
                    else showToast("Failed to get cart key.")

                }
                else
                    cartExistsCallback("e")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                showToast("Error querying the database: ${databaseError.message}")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun getProductId(prod: Products, prodidCallback: (String?) -> Unit){
        val proddb = FirebaseDatabase.getInstance().getReference("products")

        proddb.orderByChild("storeId").equalTo(prod.storeId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (prodSnap in snapshot.children)
                    {
                        val data = prodSnap.getValue(Products::class.java)
                        if (data!!.prodName == prod.prodName && data!!.prodBrand == prod.prodBrand){
                            val productId = prodSnap.key
                            prodidCallback(productId)

                            return
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showToast("Error accessing db:\n ${error.message}")
                    prodidCallback("empty")
                }
            })
    }


    private fun createCartItem(item: Products, cartId: String?, prodId: String?) {
        val cartitemdb: DatabaseReference = FirebaseDatabase.getInstance().getReference("cartitems")
        val CartItemId = cartitemdb.push().key

        var itemFound = false

        cartitemdb.orderByChild("cartId").equalTo(cartId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val itemdata = snapshot.getValue(CartItems::class.java)
                    if (itemdata?.prodId == prodId) {
                        val quantityUpdateMap = mapOf(
                            "quantity" to ServerValue.increment(1)
                        )

                        snapshot.ref.updateChildren(quantityUpdateMap)
                            .addOnCompleteListener {
                                showToast("${itemdata?.prodName} quantity updated in cart")
                            }
                            .addOnFailureListener { err ->
                                err.message?.let { showToast(it) }
                            }

                        itemFound = true
                        break
                    }
                }

                if (!itemFound) {
                    val cartitem = CartItems(
                        prodName = item.prodName,
                        prodPrice = item.prodPrice!!.toDouble(),
                        prodImg = item.prodImage,
                        totalCost = item.prodPrice,
                        cartId = cartId,
                        prodId = prodId,
                        quantity = 1
                    )

                    if (CartItemId != null) {
                        cartitemdb.child(CartItemId).setValue(cartitem)
                            .addOnCompleteListener {
                                showToast("${cartitem.prodName} added to cart")
                            }
                            .addOnFailureListener { err ->
                                err.message?.let { showToast(it) }
                            }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event
            }
        })
    }
}

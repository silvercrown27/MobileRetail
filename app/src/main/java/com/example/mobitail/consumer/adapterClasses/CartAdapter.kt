package com.example.mobitail.consumer.adapterClasses

import android.content.Context
import android.util.Log
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
import com.example.mobitail.databaseorganization.CartItems
import com.example.mobitail.databaseorganization.Secrets
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.math.log

class CartAdapter(private val context: Context) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    private val GroupList: ArrayList<CartItems> = ArrayList()

    fun setItemList(items: ArrayList<CartItems>) {
        GroupList.clear()
        GroupList.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cartitems, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = GroupList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return GroupList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.item_image)
        private val item_label: TextView = itemView.findViewById(R.id.item_label)
        private val item_currency: TextView = itemView.findViewById(R.id.currency)
        private val item_price: TextView = itemView.findViewById(R.id.item_price)
        private val deleteitem: MaterialButton = itemView.findViewById(R.id.deleteItem)

        fun bind(item: CartItems) {
            val context: Context = itemView.context

            item_label.text = item.prodName
            item_currency.text = "Ksh."
            item_price.text = item.prodPrice.toString()
            Log.d("================", "bind: ${item.prodId}")

            Glide.with(context)
                .load(item.prodImg)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)

            deleteitem.setOnClickListener {
                deleteCartItem(item.cartId, item.prodId)
            }
        }
    }

    private fun deleteCartItem(cartId: String?, prodId: String?) {
        val cartitemdb: DatabaseReference = FirebaseDatabase.getInstance()
            .getReference("cartitems")

        cartitemdb.orderByChild("prodId").equalTo(prodId).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (cartSnapshot in snapshot.children) {
                        val data = cartSnapshot.getValue(CartItems::class.java)
                        if (data!!.prodId == prodId){
                            cartSnapshot.ref.removeValue()
                            showToast("Item removed from cart")
                        }
                    }
                } else {
                    showToast("Item not found in cart")
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun getItemKey()
    {

    }
}
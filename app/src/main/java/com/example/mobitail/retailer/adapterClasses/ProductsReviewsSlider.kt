package com.example.mobitail.retailer.adapterClasses

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mobitail.R
import com.example.mobitail.databaseorganization.CustomerTable
import com.example.mobitail.databaseorganization.Reviews
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProductsReviewsSlider: RecyclerView.Adapter<ProductsReviewsSlider.ViewHolder>() {
    private val itemList = ArrayList<Reviews>()
    private var itemClickListener: OnItemClickListener? = null

    fun setItemList(items: ArrayList<Reviews>) {
        itemList.clear()
        itemList.addAll(items)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_list_rv, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userImage: ImageView = itemView.findViewById(R.id.prod_image)
        private val userEmail: TextView = itemView.findViewById(R.id.prod_label)
        private val review: TextView = itemView.findViewById(R.id.prod_label)

        fun bind(item: Reviews) {
            val context: Context = itemView.context

            getReviewer(item.reviewerId) { email, prfImg ->
                if (email != null && prfImg != null) {
                    userEmail.text = email
                    review.text = item.review

                    Glide.with(context)
                        .load(prfImg)
                        .into(userImage)
                    Log.d("Reviewer", "Email: $email, Profile Image: $prfImg")
                } else {
                    // User not found or error occurred, handle the case accordingly
                    Log.d("Reviewer", "User not found or error occurred")
                }
            }

            itemView.setOnClickListener {
                itemClickListener?.onItemClick(item)

            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: Reviews)
    }

    private fun getReviewer(userId: String?, callback: (String?, String?) -> Unit) {
        val dbref: DatabaseReference = FirebaseDatabase.getInstance().getReference("customers")

        dbref.orderByKey().equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {
                            val user = userSnapshot.getValue(CustomerTable::class.java)
                            if (user != null) {
                                val userEmail = user.email
                                val userProfileImage = user.profileimage

                                callback.invoke(userEmail, userProfileImage)
                                return
                            }
                        }
                    }

                    callback.invoke(null, null)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback.invoke(null, null)
                }
            })
    }

}
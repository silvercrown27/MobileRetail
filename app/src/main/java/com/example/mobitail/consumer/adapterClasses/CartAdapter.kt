package com.example.mobitail.consumer.adapterClasses

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.mobitail.R
import com.example.mobitail.databaseorganization.CartItems

class CartAdapter : RecyclerView.Adapter<CartAdapter.ViewHolder>() {
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

        fun bind(item: CartItems) {
            val context: Context = itemView.context

            Glide.with(context)
                .load(item.prodImg)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)

            item_label.text = item.prodName
            item_currency.text = "Ksh."
            item_price.text = item.prodPrice.toString()
        }
    }
}
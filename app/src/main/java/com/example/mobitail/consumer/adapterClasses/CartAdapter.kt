package com.example.mobitail.consumer.adapterClasses

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mobitail.R

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
        private val item_desc: TextView = itemView.findViewById(R.id.item_description)
        private val item_currency: TextView = itemView.findViewById(R.id.currency)
        private val item_procs: TextView = itemView.findViewById(R.id.item_price)

        fun bind(item: CartItems) {
            val context: Context = itemView.context

            Glide.with(context)
                .load(item.item_image)
                .into(imageView)

            item_label.text = item.item_name
            item_desc.text = item.item_desc
            item_currency.text = item.item_currency
            item_procs.text = item.item_price
        }
    }

    data class CartItems(
        val item_image: Int,
        val item_name: String,
        val item_desc: String,
        val item_currency: String,
        val item_price: String
    )
}
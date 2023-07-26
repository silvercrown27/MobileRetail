package com.example.mobitail.retailer.adapterClasses

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mobitail.R
import com.example.mobitail.databaseorganization.Products


class StoreProductsAdapter: RecyclerView.Adapter<StoreProductsAdapter.ViewHolder>() {
    private val itemList = ArrayList<Products>()
    private var itemClickListener: OnItemClickListener? = null

    fun setItemList(items: ArrayList<Products>) {
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
        private val productImage: ImageView = itemView.findViewById(R.id.prod_image)
        private val productSold: TextView = itemView.findViewById(R.id.products_sold)
        private val productName: TextView = itemView.findViewById(R.id.prodName)
        private val prodDateAdded: TextView = itemView.findViewById(R.id.dateAdded)

        fun bind(item: Products) {
            val context: Context = itemView.context

            Glide.with(context)
                .load(item.prodImage)
                .into(productImage)

            productName.text = item.prodName
            productSold.text = item.sold.toString()
            prodDateAdded.text = item.dateAdded

            itemView.setOnClickListener {
                itemClickListener?.onItemClick(item)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: Products)
    }
}
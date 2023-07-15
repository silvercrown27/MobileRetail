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
import com.example.mobitail.databaseorganization.Products
import com.google.android.material.card.MaterialCardView

class HomeItemsAdapter : RecyclerView.Adapter<HomeItemsAdapter.ViewHolder>() {
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
        private val imageView: ImageView = itemView.findViewById(R.id.groupImage)
        private val textView: TextView = itemView.findViewById(R.id.groupName)

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

            Glide.with(context)
                .load(item.prodImage)
                .into(imageView)

            textView.text = item.prodBrand
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: Products)
    }
}

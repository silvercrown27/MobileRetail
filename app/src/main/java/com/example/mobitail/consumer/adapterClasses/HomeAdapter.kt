package com.example.mobitail.consumer.adapterClasses

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mobitail.R

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    private val itemList = ArrayList<Item>()
    private var itemClickListener: OnItemClickListener? = null

    fun setItemList(items: ArrayList<Item>) {
        itemList.clear()
        itemList.addAll(items)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_view_recycle, parent, false)
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
        private val imageView: ImageView = itemView.findViewById(R.id.Category_image)
        private val textView: TextView = itemView.findViewById(R.id.Category_label)

        fun bind(item: Item) {
            val context: Context = itemView.context

            Glide.with(context)
                .load(item.imageResId)
                .into(imageView)

            textView.text = item.label

            itemView.setOnClickListener {
                itemClickListener?.onItemClick(item)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: Item)
    }

    data class Item(val imageResId: Int, val label: String)
}

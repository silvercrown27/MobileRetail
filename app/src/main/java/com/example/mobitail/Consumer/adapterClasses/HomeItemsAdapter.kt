package com.example.mobitail.Consumer.adapterClasses

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mobitail.R
import com.google.android.material.card.MaterialCardView

class HomeItemsAdapter : RecyclerView.Adapter<HomeItemsAdapter.ViewHolder>() {
    private val GroupList: ArrayList<HomeItemGroups> = ArrayList()

    fun setItemList(items: ArrayList<HomeItemGroups>) {
        GroupList.clear()
        GroupList.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemsAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_screen_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeItemsAdapter.ViewHolder, position: Int) {
        val item = GroupList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return GroupList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardView: MaterialCardView = itemView.findViewById(R.id.item_properties)
        private val imageView: ImageView = itemView.findViewById(R.id.groupImage)
        private val textView: TextView = itemView.findViewById(R.id.groupName)

        fun bind(item: HomeItemGroups) {
            val context: Context = itemView.context

            Glide.with(context)
                .load(item.courseImg)
                .into(imageView)

            textView.text = item.courseName
        }
    }

    data class HomeItemGroups(val courseName: String, val courseImg: Int)
}
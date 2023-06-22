package com.example.mobitail.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mobitail.R

class GroupsAdapter: RecyclerView.Adapter<GroupsAdapter.ViewHolder>() {
    private val GroupList: ArrayList<CategoryGroups> = ArrayList()

    fun setItemList(items: ArrayList<CategoryGroups>) {
        GroupList.clear()
        GroupList.addAll(items)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupsAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.categories_cardview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupsAdapter.ViewHolder, position: Int) {
        val item = GroupList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return GroupList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.groupImage)
        private val textView: TextView = itemView.findViewById(R.id.groupName)

        fun bind(item: CategoryGroups) {
            val context: Context = itemView.context

            Glide.with(context)
                .load(item.courseImg)
                .into(imageView)

            textView.text = item.courseName

        }
    }
    data class CategoryGroups(val courseName: String, val courseImg: Int)
}
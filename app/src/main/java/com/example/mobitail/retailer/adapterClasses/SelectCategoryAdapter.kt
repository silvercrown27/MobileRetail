package com.example.mobitail.retailer.adapterClasses

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide

import com.example.mobitail.R
import com.example.mobitail.databaseorganization.Categories



class SelectCategoryAdapter: RecyclerView.Adapter<SelectCategoryAdapter.ViewHolder>() {
    private val groupList: ArrayList<Categories> = ArrayList()
    private val selectedCategories: ArrayList<String> = ArrayList()
    private var itemClickListener: OnItemClickListener? = null

    fun setItemList(items: ArrayList<Categories>) {
        groupList.clear()
        groupList.addAll(items)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.finalsignupsteprecycleview, parent, false)
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
        private val checkbox: CheckBox = itemView.findViewById(R.id.selected_category)
        private val imageView: ImageView = itemView.findViewById(R.id.Category_image)
        private val textView: TextView = itemView.findViewById(R.id.Category_label)

        fun bind(item: Categories) {
            val context: Context = itemView.context

            Glide.with(context)
                .load(item.cat_Image)
                .into(imageView)

            textView.text = item.cat_Name

            checkbox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    item.cat_Name?.let { selectedCategories.add(it) }
                } else {
                    item.cat_Name?.let { selectedCategories.remove(it) }
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: Categories)
    }

    fun getSelectedCategories(): List<String> {
        return selectedCategories
    }
}

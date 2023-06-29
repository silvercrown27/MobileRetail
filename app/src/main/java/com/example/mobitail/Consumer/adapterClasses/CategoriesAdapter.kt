package com.example.mobitail.Consumer.adapterClasses

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobitail.R
import com.google.android.material.button.MaterialButton

class CategoriesAdapter : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {
    private val itemList = ArrayList<Item>()

    fun setItemList(items: ArrayList<Item>) {
        itemList.clear()
        itemList.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.categories_recycleview_items, parent, false)
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
        private val cardView: MaterialButton = itemView.findViewById(R.id.category_options)

        fun bind(item: Item) {
            val context: Context = itemView.context

            cardView.text = item.label

            cardView.setOnClickListener {
                val intent = Intent(context, item.activity)
                context.startActivity(intent)
            }
        }
    }

    data class Item(val label: String, val activity: Class<*>)
}
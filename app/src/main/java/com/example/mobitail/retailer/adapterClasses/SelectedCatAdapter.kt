package com.example.mobitail.retailer.adapterClasses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobitail.R

class SelectedCatAdapter : RecyclerView.Adapter<SelectedCatAdapter.ViewHolder>() {
    private val selectedCategoryLabels: ArrayList<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.finalsignupsteplist, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val label = selectedCategoryLabels[position]
        holder.bind(label)
    }

    override fun getItemCount(): Int {
        return selectedCategoryLabels.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.category_name)

        fun bind(label: String) {
            textView.text = label
        }
    }

    fun addSelectedCategory(label: String) {
        selectedCategoryLabels.add(label)
        notifyDataSetChanged()
    }
}

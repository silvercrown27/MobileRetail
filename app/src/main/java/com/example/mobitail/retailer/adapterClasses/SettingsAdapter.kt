package com.example.mobitail.retailer.adapterClasses

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

import com.example.mobitail.R


class SettingsAdapter (
    private val context: Context,
    private val itemList: ArrayList<SettingsItem>
) : ArrayAdapter<SettingsAdapter.SettingsItem>(context, 0, itemList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val listItem = itemList[position]
        val itemView = convertView ?: LayoutInflater.from(context).inflate(listItem.layoutResId, parent, false)

        val iconImageView = itemView.findViewById<ImageView>(R.id.option_icon)
        iconImageView.setImageResource(listItem.imageRes)

        val labelTextView = itemView.findViewById<TextView>(R.id.option_label)
        labelTextView.text = listItem.label

        itemView.setOnClickListener {
            val intent = Intent(context, listItem.activityClass)
            context.startActivity(intent)
        }

        return itemView
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return itemList.size
    }

    data class SettingsItem(
        val label: String,
        val layoutResId: Int,
        val imageRes: Int,
        val activityClass: Class<out Activity>
    )
}
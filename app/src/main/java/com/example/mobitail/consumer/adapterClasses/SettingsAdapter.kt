package com.example.mobitail.consumer.adapterClasses

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.mobitail.R

// SettingsAdapter.kt
class SettingsAdapter(context: Context, settingsList: List<String>) : ArrayAdapter<String>(context,
    R.layout.settings_list_item, settingsList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.settings_list_item, parent, false)
        val settingTitle = view.findViewById<TextView>(R.id.setting_title)
        settingTitle.text = getItem(position)
        return view
    }
}

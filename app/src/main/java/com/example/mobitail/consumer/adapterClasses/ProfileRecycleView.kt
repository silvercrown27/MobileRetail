package com.example.mobitail.consumer.adapterClasses

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mobitail.consumer.modalClasses.ProfileItems
import com.example.mobitail.R
import com.google.android.material.card.MaterialCardView

class ProfileRecycleView : RecyclerView.Adapter<ProfileRecycleView.ViewHolder>() {
    private val itemList = ArrayList<ProfileItems>()

    fun setItemList(items: ArrayList<ProfileItems>) {
        itemList.clear()
        itemList.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.profilegridview, parent, false)
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
        private val cardView: MaterialCardView = itemView.findViewById(R.id.profile_option)
        private val imageView: ImageView = itemView.findViewById(R.id.option_image)
        private val option_label: TextView = itemView.findViewById(R.id.option_label)
        private val option_desc: TextView = itemView.findViewById(R.id.option_description)

        fun bind(item: ProfileItems) {
            val context: Context = itemView.context

            Glide.with(context)
                .load(item.imageResId)
                .into(imageView)

            option_label.text = item.label
            option_desc.text = item.description

            cardView.setOnClickListener {
                val intent = Intent(context, item.activity)
                context.startActivity(intent)
            }
        }
    }
}

package com.example.mobitail.consumer.adapterClasses

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobitail.R
import com.example.mobitail.databaseorganization.Chats
import com.google.android.material.button.MaterialButton

class ChatsAdapter: RecyclerView.Adapter<ChatsAdapter.ViewHolder>() {
    private val chatList = ArrayList<Chats>()

    fun setChatList(chats: ArrayList<Chats>) {
        chatList.clear()
        chatList.addAll(chats)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chats_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = chatList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardView: MaterialButton = itemView.findViewById(R.id.category_options)

        fun bind(item: Chats) {
            cardView.text = item.message


        }
    }
}
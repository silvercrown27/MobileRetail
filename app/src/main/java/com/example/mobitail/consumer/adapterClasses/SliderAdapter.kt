package com.example.mobitail.consumer.adapterClasses

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.mobitail.R
import com.example.mobitail.consumer.modalClasses.SliderData
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderAdapter : SliderViewAdapter<SliderAdapter.ViewHolder>() {
    private val mSliderItems = ArrayList<SliderData>()

    fun setItemList(items: ArrayList<SliderData>){
        mSliderItems.clear()
        mSliderItems.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.slider_layout, null)
        return ViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val img = mSliderItems[position]
        holder.bind(img)
    }

    override fun getCount(): Int {
        return mSliderItems.size
    }


    inner class ViewHolder(itemView: View) : SliderViewAdapter.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.myimage)

        fun bind(item: SliderData) {
            val context: Context = itemView.context

            Glide.with(context).load(item.ImageResId).into(imageView)
        }
    }
}
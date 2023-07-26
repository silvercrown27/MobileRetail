package com.example.mobitail.consumer.adapterClasses

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.mobitail.R
import com.example.mobitail.consumer.modalClasses.SliderData
import com.example.mobitail.databaseorganization.Products
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderAdapter : SliderViewAdapter<SliderAdapter.ViewHolder>() {
    private val mSliderItems = ArrayList<Products>()

    fun setItemList(items: ArrayList<Products>){
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
        private val imageView: ImageView = itemView.findViewById(R.id.myimage)
        private val productName: TextView = itemView.findViewById(R.id.prod_name)
        private val productDesc: TextView = itemView.findViewById(R.id.prod_description)

        fun bind(item: Products) {
            val context: Context = itemView.context
            productDesc.text = item.prodDescription
            productName.text = item.prodName

            Glide.with(context).load(item.prodImage).into(imageView)
        }
    }
}

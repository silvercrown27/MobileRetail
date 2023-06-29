package com.example.mobitail.Consumer.mainActivities

//class CarouselAdapter(private val context: Context, private val itemList: List<Item>) :
//    Carousel.Adapter<CarouselAdapter.ViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.activity_home, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = itemList[position]
//        holder.bind(item)
//    }
//
//    override fun getItemCount(): Int {
//        return itemList.size
//    }
//
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val imageView: ImageView = itemView.findViewById(R.id.carousell)
//
//        fun bind(item: Item) {
//            Glide.with(context)
//                .load(item.imageResId)
//                .into(imageView)
//
//            itemView.setOnClickListener {
//                // Handle item click event
//            }
//        }
//    }
//
//    data class Item(val imageResId: Int)
//}

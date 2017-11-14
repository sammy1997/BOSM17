package com.bitspilanidvm.bosm2017.ViewHolder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bitspilanidvm.bosm2017.R

class EventItem(itemView: View) : RecyclerView.ViewHolder(itemView){
    val imageView: ImageView
    val heading: TextView
    val time: TextView
    val details: TextView

    init {
        imageView = itemView.findViewById(R.id.imageView)
        heading = itemView.findViewById(R.id.heading)
        time = itemView.findViewById(R.id.dateTime)
        details = itemView.findViewById(R.id.details)
    }
}
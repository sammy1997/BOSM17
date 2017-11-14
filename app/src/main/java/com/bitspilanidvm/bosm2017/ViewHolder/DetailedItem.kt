package com.bitspilanidvm.bosm2017.ViewHolder

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bitspilanidvm.bosm2017.R

class DetailedItem(itemView: View) : RecyclerView.ViewHolder(itemView){
    var cardView: CardView
    var imageView: ImageView
    var titleTextView: TextView
    var detailsTextView: TextView

    init {
        cardView = itemView.findViewById<CardView>(R.id.cardView)
        imageView = itemView.findViewById<ImageView>(R.id.imageView)
        titleTextView = itemView.findViewById<TextView>(R.id.heading)
        detailsTextView = itemView.findViewById<TextView>(R.id.details)
    }
}
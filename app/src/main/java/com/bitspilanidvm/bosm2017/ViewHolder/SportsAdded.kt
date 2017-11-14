package com.bitspilanidvm.bosm2017.ViewHolder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.bitspilanidvm.bosm2017.R

class SportsAdded(itemView: View) : RecyclerView.ViewHolder(itemView){
    val text: TextView

    init {
        text = itemView.findViewById(R.id.text)
    }
}
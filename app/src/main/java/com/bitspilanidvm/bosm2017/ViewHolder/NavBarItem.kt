package com.bitspilanidvm.bosm2017.ViewHolder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bitspilanidvm.bosm2017.R

class NavBarItem(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val title: TextView
    val icon: ImageView

    init {
        title = itemView.findViewById(R.id.title)
        icon = itemView.findViewById(R.id.icon)
    }
}
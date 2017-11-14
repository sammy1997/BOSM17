package com.bitspilanidvm.bosm2017.ViewHolder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Spinner
import android.widget.TextView
import com.bitspilanidvm.bosm2017.R

class ExtraEvent(itemView: View) : RecyclerView.ViewHolder(itemView){
    val sportName: TextView
    val spinner: Spinner

    init {
        sportName = itemView.findViewById(R.id.sportName)
        spinner = itemView.findViewById(R.id.spinner)
    }
}
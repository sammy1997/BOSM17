package com.bitspilanidvm.bosm2017.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bitspilanidvm.bosm2017.R
import com.bitspilanidvm.bosm2017.ViewHolder.SportsAdded

class SportsAdded(val names: ArrayList<String>) : RecyclerView.Adapter<SportsAdded>(){

    override fun onBindViewHolder(holder: SportsAdded, position: Int) {
        holder.text.text = names[position]
    }

    override fun getItemCount() = names.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SportsAdded {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sports_added_layout, parent, false)
        return SportsAdded(view)
    }
}
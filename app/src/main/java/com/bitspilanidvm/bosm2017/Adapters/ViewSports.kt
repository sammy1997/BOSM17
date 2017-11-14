package com.bitspilanidvm.bosm2017.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bitspilanidvm.bosm2017.ClickListeners.ViewSportsClickListener
import com.bitspilanidvm.bosm2017.R
import com.bitspilanidvm.bosm2017.ViewHolder.SportsAdded

class ViewSports(val names: ArrayList<String>, val listener: ViewSportsClickListener) : RecyclerView.Adapter<SportsAdded>(){

    override fun onBindViewHolder(holder: SportsAdded, position: Int) {
        holder.text.text = names[position]
        holder.itemView.setOnClickListener { listener.onItemClick(holder, position) }
    }

    override fun getItemCount() = names.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SportsAdded {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sports_added_layout, parent, false)
        return SportsAdded(view)
    }
}
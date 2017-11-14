package com.bitspilanidvm.bosm2017.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bitspilanidvm.bosm2017.ClickListeners.ItemClickListener
import com.bitspilanidvm.bosm2017.R
import com.bitspilanidvm.bosm2017.ViewHolder.NavBarItem

class NavBarAdapter(val items: Array<com.bitspilanidvm.bosm2017.Modals.NavBarItem>, val listener: ItemClickListener) : RecyclerView.Adapter<NavBarItem>(){

    override fun onBindViewHolder(holder: NavBarItem, position: Int) {
        holder.title.text = items[position].text
        holder.icon.setImageResource(items[position].image)
        holder.itemView.setOnClickListener { listener.onItemClicked(position) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavBarItem {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.navbar_item, parent, false)
        return NavBarItem(view)
    }

    override fun getItemCount() = items.size
}
package com.bitspilanidvm.bosm2017.Adapters

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bitspilanidvm.bosm2017.R
import com.bitspilanidvm.bosm2017.Universal.EventItem
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator

class EventItem(val item: EventItem) : RecyclerView.Adapter<com.bitspilanidvm.bosm2017.ViewHolder.EventItem>(){

    override fun onBindViewHolder(holder: com.bitspilanidvm.bosm2017.ViewHolder.EventItem, position: Int) {
        //holder.imageView.setImageResource(item.imageRes)
        picasso(holder.imageView.context, item.imageRes).into(holder.imageView)
        holder.heading.text = item.heading
        holder.time.text = item.time
        holder.details.text = item.details

        val typeface = Typeface.createFromAsset(holder.imageView.context.assets, "fonts/Coves-Bold.otf")
        holder.heading.typeface = typeface
        holder.time.typeface = typeface
        holder.details.typeface = typeface
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): com.bitspilanidvm.bosm2017.ViewHolder.EventItem {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_item_layout, parent, false)
        return com.bitspilanidvm.bosm2017.ViewHolder.EventItem(view)
    }

    override fun getItemCount() = 1

    fun picasso(context: Context, resourceId: Int): RequestCreator {
        return Picasso.with(context)
                .load(resourceId)
                .fit()
                .centerCrop()
    }
}
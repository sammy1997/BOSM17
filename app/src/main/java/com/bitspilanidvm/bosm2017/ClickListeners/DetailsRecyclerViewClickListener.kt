package com.bitspilanidvm.bosm2017.ClickListeners

import com.bitspilanidvm.bosm2017.ViewHolder.DetailedItem

interface DetailsRecyclerViewClickListener {
    fun onItemClick(itemHolder : DetailedItem, position: Int)
}
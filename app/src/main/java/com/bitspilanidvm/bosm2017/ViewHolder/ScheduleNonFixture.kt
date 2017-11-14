package com.bitspilanidvm.bosm2017.ViewHolder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.bitspilanidvm.bosm2017.R
import com.sackcentury.shinebuttonlib.ShineButton

class ScheduleNonFixture(itemView: View) : RecyclerView.ViewHolder(itemView){
    val round: TextView
    val categoryName: TextView
    val categoryDescription: TextView
    val tdv: TextView
    val categoryNameText: TextView
    val categoryDescriptionText: TextView
    val shineButton: ShineButton

    init {
        round = itemView.findViewById(R.id.round)
        categoryName = itemView.findViewById(R.id.categoryName)
        categoryDescription = itemView.findViewById(R.id.categoryDescription)
        tdv = itemView.findViewById(R.id.tdv)
        categoryNameText = itemView.findViewById(R.id.categoryNameText)
        categoryDescriptionText = itemView.findViewById(R.id.categoryDescriptionText)
        shineButton = itemView.findViewById(R.id.shineButton)
    }
}
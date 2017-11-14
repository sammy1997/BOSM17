package com.bitspilanidvm.bosm2017.ViewHolder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.bitspilanidvm.bosm2017.R

class ResultsNonFixture(itemView: View) : RecyclerView.ViewHolder(itemView){
    val round: TextView
    val categoryName: TextView
    val categoryDescription: TextView
    val categoryNameText: TextView
    val categoryDescriptionText: TextView
    val tdv: TextView
    val first: TextView
    val second: TextView
    val third: TextView
    val firstText: TextView
    val secondText: TextView
    val thirdText: TextView
    val firstScore: TextView
    val secondScore: TextView
    val thirdScore: TextView

    init {
        round = itemView.findViewById(R.id.round)
        categoryName = itemView.findViewById(R.id.categoryName)
        categoryDescription = itemView.findViewById(R.id.categoryDescription)
        categoryNameText = itemView.findViewById(R.id.categoryNameText)
        categoryDescriptionText = itemView.findViewById(R.id.categoryDescriptionText)
        tdv = itemView.findViewById(R.id.tdv)
        first = itemView.findViewById(R.id.first)
        second = itemView.findViewById(R.id.second)
        third = itemView.findViewById(R.id.third)
        firstText = itemView.findViewById(R.id.firstText)
        secondText = itemView.findViewById(R.id.secondText)
        thirdText = itemView.findViewById(R.id.thirdText)
        firstScore = itemView.findViewById(R.id.firstScore)
        secondScore = itemView.findViewById(R.id.secondScore)
        thirdScore = itemView.findViewById(R.id.thirdScore)
    }
}
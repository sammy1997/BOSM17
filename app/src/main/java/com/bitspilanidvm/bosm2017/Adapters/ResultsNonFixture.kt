package com.bitspilanidvm.bosm2017.Adapters

import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bitspilanidvm.bosm2017.R
import com.bitspilanidvm.bosm2017.Universal.NonFixtureSportsDataDecoupled
import com.bitspilanidvm.bosm2017.ViewHolder.ResultsNonFixture
import java.text.SimpleDateFormat

class ResultsNonFixture(val data: List<NonFixtureSportsDataDecoupled>, val typeface: Typeface) : RecyclerView.Adapter<ResultsNonFixture>(){

    val dateFormat = SimpleDateFormat("MMM dd yyyy HH:mm:ss")
    val formattedDate = SimpleDateFormat("MMMM dd")
    val formattedTime = SimpleDateFormat("HH:mm")

    override fun onBindViewHolder(holder: ResultsNonFixture, position: Int) {
        val date = dateFormat.parse("${data[position].date} ${data[position].time}")
        holder.tdv.text = "${formattedDate.format(date)} | ${formattedTime.format(date)} | ${data[position].venue}"
        holder.categoryName.text = data[position].categoryName
        holder.categoryDescription.text = data[position].categoryDescription
        holder.round.text = data[position].round
        if (data[position].categoryResult.size == 1){
            holder.first.text = data[position].categoryResult[0]
            holder.second.visibility = View.INVISIBLE
            holder.third.visibility = View.INVISIBLE

            holder.firstScore.text = data[position].categoryScore[0] ?: ""
            holder.secondScore.visibility = View.INVISIBLE
            holder.thirdScore.visibility = View.INVISIBLE
        }else if (data[position].categoryResult.size == 2){
            holder.first.text = data[position].categoryResult[0]
            holder.second.text = data[position]?.categoryResult[1]
            holder.third.visibility = View.INVISIBLE

            holder.firstScore.text = data[position].categoryScore[0] ?: ""
            holder.secondScore.text = data[position].categoryScore[1] ?: ""
            holder.thirdScore.visibility = View.INVISIBLE
        }else{
            holder.first.text = data[position].categoryResult[0]
            holder.second.text = data[position]?.categoryResult[1]
            holder.third.text = data[position].categoryResult[2]

            holder.firstScore.text = data[position].categoryScore[0] ?: ""
            holder.secondScore.text = data[position].categoryScore[1] ?: ""
            holder.thirdScore.text = data[position].categoryScore[2] ?: ""
        }

        holder.round.typeface = typeface
        holder.categoryName.typeface = typeface
        holder.categoryDescription.typeface = typeface
        holder.categoryNameText.typeface = typeface
        holder.categoryDescriptionText.typeface = typeface
        holder.tdv.typeface = typeface
        holder.first.typeface = typeface
        holder.second.typeface = typeface
        holder.third.typeface = typeface
        holder.firstScore.typeface = typeface
        holder.secondScore.typeface = typeface
        holder.thirdScore.typeface = typeface
        holder.firstText.typeface = typeface
        holder.secondText.typeface = typeface
        holder.thirdText.typeface = typeface


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsNonFixture {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.results_nonfixture_item_layout_new, parent, false)
        return com.bitspilanidvm.bosm2017.ViewHolder.ResultsNonFixture(view)
    }

    override fun getItemCount() = data.size
}
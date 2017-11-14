package com.bitspilanidvm.bosm2017.Adapters

import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bitspilanidvm.bosm2017.ClickListeners.StarClickListener
import com.bitspilanidvm.bosm2017.R
import com.bitspilanidvm.bosm2017.Universal.GLOBAL_DATA
import com.bitspilanidvm.bosm2017.Universal.NonFixtureSportsDataDecoupled
import com.bitspilanidvm.bosm2017.ViewHolder.ScheduleNonFixture
import com.sackcentury.shinebuttonlib.ShineButton
import java.text.SimpleDateFormat

class ScheduleNonFixture(val data: List<NonFixtureSportsDataDecoupled>, val typeface: Typeface, val starClickListener: StarClickListener?, val starred: ArrayList<String>?, val name: String?, val position: Int?, val isOngoing: Boolean = false) : RecyclerView.Adapter<ScheduleNonFixture>(){

    val dateFormat = SimpleDateFormat("MMM dd yyyy HH:mm:ss")
    val formattedDate = SimpleDateFormat("MMMM dd")
    val formattedTime = SimpleDateFormat("HH:mm")

    override fun onBindViewHolder(holder: ScheduleNonFixture, position: Int) {
        holder.categoryName.text = data[position].categoryName
        holder.categoryDescription.text = data[position].categoryDescription
        holder.round.text = data[position].round
        val date = dateFormat.parse("${data[position].date} ${data[position].time}")
        holder.tdv.text = "${formattedDate.format(date)} | ${formattedTime.format(date)} | ${data[position].venue}"

        holder.round.typeface = typeface
        holder.tdv.typeface = typeface
        holder.categoryName.typeface = typeface
        holder.categoryDescription.typeface = typeface
        holder.categoryNameText.typeface = typeface
        holder.categoryDescriptionText.typeface = typeface

        if (isOngoing)
            holder.shineButton.visibility = View.INVISIBLE

        if (System.currentTimeMillis() > date.time)
            holder.shineButton.visibility = View.INVISIBLE

        if (starClickListener != null && starred != null && name != null && position != null) {
            val key = "${data[position].round}${formattedDate.format(date)}${formattedTime.format(date)}${data[position].venue}${data[position].categoryName}${data[position].categoryDescription}"
            val text = "The event ${data[position].round} ${data[position].categoryName} ${data[position].categoryDescription} is going to start in about 30 minutes from now at ${data[position].venue}. Don't miss it out!"

            if (key in starred)
                holder.shineButton.isChecked = true

            holder.shineButton.setOnClickListener {
                starClickListener.onStarClicked(key, (it as ShineButton).isChecked, name, text, GLOBAL_DATA.sportsImageIconRes[name] ?: R.drawable.ic_cricket, date)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleNonFixture {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.schedule_nonfixture_item_layout_new, parent, false)
        return com.bitspilanidvm.bosm2017.ViewHolder.ScheduleNonFixture(view)
    }

    override fun getItemCount() = data.size
}
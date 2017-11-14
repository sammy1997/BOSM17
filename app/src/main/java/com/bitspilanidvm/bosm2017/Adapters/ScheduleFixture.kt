package com.bitspilanidvm.bosm2017.Adapters

import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bitspilanidvm.bosm2017.ClickListeners.StarClickListener
import com.bitspilanidvm.bosm2017.Modals.FixtureSportsData
import com.bitspilanidvm.bosm2017.R
import com.bitspilanidvm.bosm2017.Universal.GLOBAL_DATA
import com.bitspilanidvm.bosm2017.ViewHolder.ScheduleFixture
import com.sackcentury.shinebuttonlib.ShineButton
import java.text.SimpleDateFormat

class ScheduleFixture(val data: List<FixtureSportsData>, val typeface: Typeface, val starClickListener: StarClickListener?, val starred: ArrayList<String>?, val name: String?, val position: Int?, val isOngoing: Boolean = false) : RecyclerView.Adapter<ScheduleFixture>(){

    val dateFormat = SimpleDateFormat("MMM dd yyyy HH:mm:ss")
    val formattedDate = SimpleDateFormat("MMMM dd")
    val formattedTime = SimpleDateFormat("HH:mm")

    override fun onBindViewHolder(holder: ScheduleFixture, position: Int) {
        val date = dateFormat.parse("${data[position].date} ${data[position].time}")
        holder.tdv.text = "${formattedDate.format(date)} | ${formattedTime.format(date)} | ${data[position].venue}"
        holder.round.text = data[position].round
        holder.teamA.text = data[position].teamA
        holder.teamB.text = data[position].teamB

        holder.round.typeface = typeface
        holder.tdv.typeface = typeface
        holder.teamA.typeface = typeface
        holder.teamB.typeface = typeface
        holder.vs.typeface = typeface

        if (isOngoing)
            holder.shineButton.visibility = View.INVISIBLE

        if (System.currentTimeMillis() > date.time)
            holder.shineButton.visibility = View.INVISIBLE

        if (starClickListener != null && starred != null && name != null && position != null) {
            val key = "${data[position].round}${formattedDate.format(date)}${formattedTime.format(date)}${data[position].venue}${data[position].teamA}${data[position].teamB}"
            val text = "The $name ${data[position].round} match between ${data[position].teamA} and ${data[position].teamB} is going to start in about 30 minutes from now at ${data[position].venue}. Don't miss it out!"

            if (key in starred)
                holder.shineButton.isChecked = true

            holder.shineButton.setOnClickListener {
                starClickListener.onStarClicked(key, (it as ShineButton).isChecked, name, text, GLOBAL_DATA.sportsImageIconRes[name] ?: R.drawable.ic_cricket, date)
            }
        }
    }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleFixture {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.schedule_fixture_item_layout_new, parent, false)
        return com.bitspilanidvm.bosm2017.ViewHolder.ScheduleFixture(view)
    }
}
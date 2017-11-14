package com.bitspilanidvm.bosm2017.ViewHolder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.bitspilanidvm.bosm2017.R
import com.sackcentury.shinebuttonlib.ShineButton

class ScheduleFixture(itemView: View) : RecyclerView.ViewHolder(itemView){
    val round: TextView
    val teamA: TextView
    val teamB: TextView
    val tdv: TextView
    val vs: TextView
    val shineButton: ShineButton

    init {
        round = itemView.findViewById(R.id.round)
        teamB = itemView.findViewById(R.id.teamA)
        teamA = itemView.findViewById(R.id.teamB)
        tdv = itemView.findViewById(R.id.tdv)
        vs = itemView.findViewById(R.id.vs)
        shineButton = itemView.findViewById(R.id.shineButton)
    }
}
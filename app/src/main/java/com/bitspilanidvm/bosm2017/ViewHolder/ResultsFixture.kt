package com.bitspilanidvm.bosm2017.ViewHolder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.bitspilanidvm.bosm2017.R

class ResultsFixture(itemView: View) : RecyclerView.ViewHolder(itemView){
    val round: TextView
    val teamA: TextView
    val teamB: TextView
    val tdv: TextView
    val teamAScore: TextView
    val teamBScore: TextView
    val winner: TextView
    val winnerText: TextView

    init {
        round = itemView.findViewById(R.id.round)
        teamA = itemView.findViewById(R.id.teamA)
        teamB = itemView.findViewById(R.id.teamB)
        tdv = itemView.findViewById(R.id.tdv)
        teamAScore = itemView.findViewById(R.id.teamAScore)
        teamBScore = itemView.findViewById(R.id.teamBScore)
        winner = itemView.findViewById(R.id.winner)
        winnerText = itemView.findViewById(R.id.winnerText)
    }
}
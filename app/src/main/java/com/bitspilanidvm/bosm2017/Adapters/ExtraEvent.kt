package com.bitspilanidvm.bosm2017.Adapters

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.bitspilanidvm.bosm2017.R
import com.bitspilanidvm.bosm2017.ViewHolder.ExtraEvent
import org.json.JSONArray

class ExtraEvent(val participants: JSONArray, val eventsName: ArrayList<String>, val mapToParticipantsID: HashMap<Int, Int>, val mapToEventsID: HashMap<String, Int>, val eventsData: HashMap<Int, ArrayList<Int>>) : RecyclerView.Adapter<ExtraEvent>(){

    override fun getItemCount() = participants.length()

    override fun onBindViewHolder(holder: ExtraEvent, position: Int) {
        holder.sportName.text = participants.getJSONObject(position).getString("name")
        holder.spinner.adapter = ArrayAdapter<String>(holder.sportName.context, android.R.layout.simple_dropdown_item_1line, eventsName)
        holder.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                val eventID = mapToEventsID[eventsName[pos]] ?: 0
                val participantID = mapToParticipantsID[position] ?: 0

                Log.e("eventID", "$eventID")
                Log.e("participantID", "$participantID")

                if (pos > 0){
                    if (eventID !in eventsData.keys){
                        val arrayList = ArrayList<Int>()
                        arrayList.add(participantID)
                        eventsData.put(eventID, arrayList)
                    }else{
                        eventsData[eventID]?.add(participantID)
                    }
                }else{
                    for (i in eventsData.keys)
                        eventsData[i]?.remove(participantID)
                }

                Log.e("events", eventsData.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExtraEvent {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.extra_event_choice, parent, false)
        return ExtraEvent(view)
    }
}
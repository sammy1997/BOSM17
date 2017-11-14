package com.bitspilanidvm.bosm2017.Fragments

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.bitspilanidvm.bosm2017.R
import org.json.JSONArray

class Decision : Fragment(){

    lateinit var viewAddedPlayers: Button
    lateinit var addAnother: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_decision, container, false)
        viewAddedPlayers = view.findViewById(R.id.viewAddedPlayers)
        addAnother = view.findViewById(R.id.addAnother)

        viewAddedPlayers.setOnClickListener {

            val participantsJSONArray = JSONArray(arguments.getString("players"))
            Log.e("pfsd", participantsJSONArray.toString(4))
            val participants = ArrayList<String>()

            for (i in 0..(participantsJSONArray.length() - 1))
                participants.add(participantsJSONArray.getJSONObject(i).getString("name"))

            if(participants.isNotEmpty()){
                val viewParticipants = ViewParticipants()
                val arguments = Bundle()
                arguments.putStringArrayList("participants", participants)
                viewParticipants.arguments = arguments
                activity.supportFragmentManager.beginTransaction().replace(R.id.rootConstraintLayout, viewParticipants).commit()
            }else{
                Snackbar.make(view, "No Existing Participants. Tap Add Another to add one.", Snackbar.LENGTH_LONG).show()
            }
        }

        addAnother.setOnClickListener {
            val sportName = arguments.getString("sportName")
            val formFragment = Form()
            val arguments = Bundle()
            arguments.putString("sportName", "$sportName")
            formFragment.arguments = arguments
            activity.supportFragmentManager.beginTransaction().replace(R.id.rootConstraintLayout, formFragment).commit()
        }

        return view
    }
}
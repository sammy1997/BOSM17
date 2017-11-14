package com.bitspilanidvm.bosm2017.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bitspilanidvm.bosm2017.Activity.Main
import com.bitspilanidvm.bosm2017.Adapters.ExtraEvent
import com.bitspilanidvm.bosm2017.R
import com.bitspilanidvm.bosm2017.Universal.URL
import com.dd.processbutton.iml.ActionProcessButton
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject

class AddExtraEvents : Fragment(){

    lateinit var hamburgerIcon: ImageView
    lateinit var participants: RecyclerView
    lateinit var done: ActionProcessButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_extra_event, container, false)
        hamburgerIcon = view.findViewById(R.id.hamburgerIcon)
        participants = view.findViewById(R.id.participants)
        done = view.findViewById(R.id.done)

        hamburgerIcon.setOnClickListener {
            if (activity.drawerLayout.isDrawerOpen(GravityCompat.START))
                activity.drawerLayout.closeDrawer(GravityCompat.START)
            else
                activity.drawerLayout.openDrawer(GravityCompat.START)
        }

        val captain_id = arguments.getInt("tc_id")
        val data = JSONObject(arguments.getString("jsonData"))

        val eventsName = ArrayList<String>()
        val mapToParticipantsID = HashMap<Int, Int>()
        val mapToEventsID = HashMap<String, Int>()

        eventsName.add("none")
        mapToEventsID.put("none", 0)

        for (i in 0..(data.getJSONArray("participants").length() - 1))
            mapToParticipantsID.put(i, data.getJSONArray("participants").getJSONObject(i).getInt("id"))


        for (i in 0..(data.getJSONArray("events").length() - 1)) {
            eventsName.add(data.getJSONArray("events").getJSONObject(i).getString("name"))
            mapToEventsID.put(data.getJSONArray("events").getJSONObject(i).getString("name"),
                    data.getJSONArray("events").getJSONObject(i).getInt("id"))
        }

        Log.e("mapToParticipantsID", mapToParticipantsID.toString())
        Log.e("mapToEventsID", mapToEventsID.toString())

        val eventsData = HashMap<Int, ArrayList<Int>>()
        val layoutManager = LinearLayoutManager(activity)
        participants.layoutManager = layoutManager
        participants.adapter = ExtraEvent(data.getJSONArray("participants"), eventsName, mapToParticipantsID, mapToEventsID, eventsData)

        done.setOnClickListener {
            done.setMode(ActionProcessButton.Mode.ENDLESS)
            done.progress = 1

            Log.e("sdf", eventsData.toString())
            var finalData = JSONObject()
            val partArray = JSONArray()

            for (i in eventsData.keys){
                val x = JSONArray()
                x.put(i)
                for (j in eventsData[i] ?: ArrayList())
                    x.put(j)

                partArray.put(x.toString())
            }

            finalData.put("part_data", partArray.toString())
            Log.e("final Data", finalData.toString())

            val authenticationDetails = JSONObject()
            authenticationDetails.put("username", (activity as Main).username)
            authenticationDetails.put("password", (activity as Main).password)

            AndroidNetworking.post(URL.API_TOKEN)
                    .addJSONObjectBody(authenticationDetails)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject) {

                            val token = response.getString("token")

                            AndroidNetworking.post(URL.ADD_EXTRA_EVENT(captain_id))
                                    .addHeaders("Authorization", "JWT $token")
                                    .addJSONObjectBody(finalData)
                                    .build()
                                    .getAsJSONObject(object : JSONObjectRequestListener {
                                        override fun onResponse(response: JSONObject) {
                                            done.progress = 100
                                            activity.supportFragmentManager.beginTransaction().replace(R.id.rootConstraintLayout, ManageSports()).commit()
                                        }

                                        override fun onError(anError: ANError) {
                                            Log.e("error", anError.errorBody)
                                            done.progress = -1
                                        }
                                    })
                        }

                        override fun onError(anError: ANError?) {
                            done.progress = -1
                        }
                    })
        }

        return view
    }
}

package com.bitspilanidvm.bosm2017.Fragments

import android.os.Bundle
import android.support.design.widget.Snackbar
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
import com.bitspilanidvm.bosm2017.Adapters.ViewSports
import com.bitspilanidvm.bosm2017.ClickListeners.ViewSportsClickListener
import com.bitspilanidvm.bosm2017.R
import com.bitspilanidvm.bosm2017.Universal.URL
import com.dd.processbutton.iml.ActionProcessButton
import com.google.gson.JsonParseException
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject

class ViewSports : Fragment(){

    lateinit var hamburgerIcon: ImageView
    lateinit var recyclerView: RecyclerView
    lateinit var manageSports: ActionProcessButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_view_sports, container, false)

        hamburgerIcon = view.findViewById(R.id.hamburgerIcon)
        recyclerView = view.findViewById(R.id.recycler_view)
        manageSports = view.findViewById(R.id.manageSport)

        hamburgerIcon.setOnClickListener {
            if (activity.drawerLayout.isDrawerOpen(GravityCompat.START))
                activity.drawerLayout.closeDrawer(GravityCompat.START)
            else
                activity.drawerLayout.openDrawer(GravityCompat.START)
        }

        val currentSports = JSONArray(arguments.getString("currentSports"))
        val sportsName = ArrayList<String>()

        for (i in 0..(currentSports.length() - 1)){
            sportsName.add(currentSports.getJSONObject(i).getString("name"))
        }

        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = ViewSports(sportsName, object : ViewSportsClickListener {
                override fun onItemClick(itemHolder: com.bitspilanidvm.bosm2017.ViewHolder.SportsAdded, position: Int) {

                    var id = 0

                    for (sport in (activity as Main).addedSports){
                        if (sport.getString("name") == itemHolder.text.text){
                            id = sport.getInt("id")
                            break
                        }
                    }

                    val authenticationDetails = JSONObject()
                    authenticationDetails.put("username", (activity as Main).username)
                    authenticationDetails.put("password", (activity as Main).password)

                    AndroidNetworking.post(URL.API_TOKEN)
                            .addJSONObjectBody(authenticationDetails)
                            .build()
                            .getAsJSONObject(object : JSONObjectRequestListener{
                                override fun onResponse(response: JSONObject) {

                                    val token = response.getString("token")

                                    AndroidNetworking.get(URL.SPORT_TEST(id))
                                            .addHeaders("Authorization", "JWT $token")
                                            .build()
                                            .getAsJSONObject(object : JSONObjectRequestListener{
                                                override fun onResponse(response: JSONObject) {

                                                    if (response.has("participants")){
                                                        val viewParticipants = ViewParticipants()
                                                        val arguments = Bundle()
                                                        val participantsJSONArray = response.getJSONArray("participants")
                                                        val participants = ArrayList<String>()

                                                        for (i in 0..(participantsJSONArray.length() - 1))
                                                            participants.add(participantsJSONArray.getJSONObject(i).getString("name"))

                                                        viewParticipants.arguments = arguments

                                                        arguments.putStringArrayList("participants", participants)

                                                        activity.supportFragmentManager.beginTransaction().replace(R.id.rootConstraintLayout, viewParticipants).commit()
                                                    }else if (response.getString("message") == "Single Event"){

                                                        val decisionFragment = Decision()
                                                        val arguments = Bundle()
                                                        arguments.putString("sportName", "${itemHolder.text.text}")

                                                        val participantsJSONArray = response.getJSONArray("players")
                                                        /*val participants = ArrayList<String>()

                                                        for (i in 0..(participantsJSONArray.length() - 1))
                                                            participants.add(participantsJSONArray.getJSONObject(i).getString("name"))*/

                                                        arguments.putString("players", "$participantsJSONArray")
                                                        decisionFragment.arguments = arguments

                                                        activity.supportFragmentManager.beginTransaction().replace(R.id.rootConstraintLayout, decisionFragment).commit()
                                                    }
                                                    else{
                                                        Log.e("response", response.toString(4))
                                                        val formFragment = Form()
                                                        val arguments = Bundle()
                                                        arguments.putString("sportName", "${itemHolder.text.text}")
                                                        formFragment.arguments = arguments
                                                        (activity as Main).shouldBeHandledBySystem = true
                                                        val transaction = activity.supportFragmentManager.beginTransaction()
                                                        transaction.replace(R.id.rootConstraintLayout, formFragment)
                                                        transaction.addToBackStack(null)
                                                        transaction.commit()
                                                    }
                                                }

                                                override fun onError(anError: ANError?) {

                                                }
                                            })
                                }

                                override fun onError(anError: ANError?) {
                                }
                            })
            }
        })

        manageSports.setOnClickListener {
            manageSports.setMode(ActionProcessButton.Mode.ENDLESS)
            manageSports.progress = 1

            val data = JSONObject()
            data.put("username", (activity as Main).username)
            data.put("password", (activity as Main).password)

            AndroidNetworking.post(URL.API_TOKEN)
                    .addJSONObjectBody(data)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject) {
                            Log.e("response", response.toString())

                            AndroidNetworking.get(URL.SHOW_SPORTS)
                                    .addHeaders("Authorization", "JWT ${response.getString("token")}")
                                    .build()
                                    .getAsJSONObject(object : JSONObjectRequestListener {
                                        override fun onResponse(response: JSONObject) {
                                            manageSports.progress = 100

                                            var sportsAddedArray = JSONArray()
                                            var sportsLeftArray = JSONArray()

                                            if (response.has("sports_added"))
                                                sportsAddedArray = response.getJSONArray("sports_added")

                                            if (response.has("sports_left"))
                                                sportsLeftArray = response.getJSONArray("sports_left")

                                            (activity as Main).addedSports.clear()
                                            (activity as Main).availableSports.clear()

                                            for (i in 0..(sportsAddedArray.length() - 1))
                                                (activity as Main).addedSports.add(sportsAddedArray.getJSONObject(i))

                                            for (i in 0..(sportsLeftArray.length() - 1))
                                                (activity as Main).availableSports.add(sportsLeftArray.getJSONObject(i))

                                            activity.supportFragmentManager.beginTransaction().replace(R.id.rootConstraintLayout, ManageSports()).commit()
                                        }

                                        override fun onError(anError: ANError) {
                                            manageSports.progress = -1
                                        }
                                    })
                        }

                        override fun onError(anError: ANError) {
                            Log.e("error", anError.errorBody)
                            try {
                                val error = JSONObject(anError.errorBody)
                                if (error.has("non_field_errors"))
                                    Snackbar.make(view, error.getJSONArray("non_field_errors")[0].toString(), Snackbar.LENGTH_LONG).show()
                            }catch (e: JsonParseException){

                            }
                            manageSports.progress = -1
                        }
                    })
        }

        return view
    }
}
package com.bitspilanidvm.bosm2017.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bitspilanidvm.bosm2017.Activity.Main
import com.bitspilanidvm.bosm2017.Adapters.SportsAdded
import com.bitspilanidvm.bosm2017.R
import com.bitspilanidvm.bosm2017.Universal.URL
import com.dd.processbutton.iml.ActionProcessButton
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject

class ManageSports : Fragment(){

    lateinit var hamburgerIcon: ImageView
    lateinit var spinner: Spinner
    lateinit var recyclerView: RecyclerView
    lateinit var addSport: Button
    lateinit var done: ActionProcessButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_manage_sports, container, false)

        hamburgerIcon = view.findViewById(R.id.hamburgerIcon)
        recyclerView = view.findViewById(R.id.sportsAdded)
        spinner = view.findViewById(R.id.sportsLeft)
        addSport = view.findViewById(R.id.addSport)
        done = view.findViewById(R.id.done)

        val addedSportsList = ArrayList<String>()
        val availableSportsList = ArrayList<String>()
        val addedSportsID = ArrayList<Int>()
        val availableSportsID = ArrayList<Int>()

        val amendments = HashMap<Int, String>()

        val mainActivity = activity as Main

        mainActivity.addedSports.forEach { obj ->
            addedSportsList.add(obj.getString("name"))
            addedSportsID.add(obj.getInt("id"))
        }

        mainActivity.availableSports.forEach { obj ->
            availableSportsList.add(obj.getString("name"))
            availableSportsID.add(obj.getInt("id"))
        }

        val adapter = ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, availableSportsList)
        spinner.adapter = adapter

        recyclerView.layoutManager = LinearLayoutManager(activity)

        recyclerView.adapter = SportsAdded(addedSportsList)


        hamburgerIcon.setOnClickListener {
            if (activity.drawerLayout.isDrawerOpen(GravityCompat.START))
                activity.drawerLayout.closeDrawer(GravityCompat.START)
            else
                activity.drawerLayout.openDrawer(GravityCompat.START)
        }

        val simpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val position = viewHolder.adapterPosition

                amendments[addedSportsID[position]] = "R"

                availableSportsList.add(addedSportsList[position])
                availableSportsID.add(addedSportsID[position])
                adapter.notifyDataSetChanged()

                addedSportsList.removeAt(position)
                addedSportsID.removeAt(position)
                (recyclerView.adapter).notifyItemRemoved(position)

                Log.e("sports added", amendments.toString())
            }
        }
        val touchHelper = ItemTouchHelper(simpleCallback)
        touchHelper.attachToRecyclerView(recyclerView)


        addSport.setOnClickListener {

            val position = spinner.selectedItemPosition

            amendments[availableSportsID[position]] = "A"

            addedSportsList.add(0, availableSportsList[position])
            addedSportsID.add(0, availableSportsID[position])
            (recyclerView.adapter).notifyItemInserted(0)
            recyclerView.smoothScrollToPosition(0)

            availableSportsList.removeAt(position)
            availableSportsID.removeAt(position)
            adapter.notifyDataSetChanged()

            Log.e("sports added", amendments.toString())
        }

        done.setOnClickListener {

            done.setMode(ActionProcessButton.Mode.ENDLESS)
            done.progress = 1

            val finalAmendments = HashMap<Int, String>()

            for ((k, v) in amendments)
                finalAmendments[k] = v

            for ((id, status) in amendments){

                mainActivity.addedSports.forEach { obj ->
                    if (status == "A" && id == obj.getInt("id"))
                        finalAmendments.remove(id)
                }
                mainActivity.availableSports.forEach { obj ->
                    if (status == "R" && id == obj.getInt("id"))
                        finalAmendments.remove(id)
                }
            }
            Log.e("final amendments", finalAmendments.toString())

            val sportsAdded = JSONArray()
            val sportsLeft = JSONArray()
            for ((k, v) in finalAmendments){
                if (v == "A")
                    sportsAdded.put(k)
                if (v == "R")
                    sportsLeft.put(k)
            }

            val authenticationDetails = JSONObject()
            authenticationDetails.put("username", mainActivity.username)
            authenticationDetails.put("password", mainActivity.password)

            val addRemoveDetails = JSONObject()
            addRemoveDetails.put("sportsadded", sportsAdded.toString())
            addRemoveDetails.put("sportsleft", sportsLeft.toString())

            AndroidNetworking.post(URL.API_TOKEN)
                    .addJSONObjectBody(authenticationDetails)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener{
                        override fun onResponse(response: JSONObject) {
                            AndroidNetworking.post(URL.MANAGE_SPORTS)
                                    .addHeaders("Authorization", "JWT ${response.getString("token")}")
                                    .addJSONObjectBody(addRemoveDetails)
                                    .build()
                                    .getAsJSONObject(object : JSONObjectRequestListener{
                                        override fun onResponse(response: JSONObject) {

                                            var sportsAddedArray = JSONArray()
                                            var sportsLeftArray = JSONArray()

                                            if (response.has("sports_added"))
                                                sportsAddedArray = response.getJSONArray("sports_added")

                                            if (response.has("sports_left"))
                                                sportsLeftArray = response.getJSONArray("sports_left")
                                            try {
                                                (activity as Main).addedSports.clear()
                                                (activity as Main).availableSports.clear()

                                                for (i in 0..(sportsAddedArray.length() - 1))
                                                    (activity as Main).addedSports.add(sportsAddedArray.getJSONObject(i))

                                                for (i in 0..(sportsLeftArray.length() - 1))
                                                    (activity as Main).availableSports.add(sportsLeftArray.getJSONObject(i))
                                            }catch (e: TypeCastException){
                                                e.printStackTrace()
                                            }
                                            var currentSports = JSONArray()
                                            if (response.has("sports_added"))
                                                currentSports = response.getJSONArray("sports_added")

                                            val bundle = Bundle()
                                            done.progress = 100
                                            bundle.putString("currentSports", currentSports.toString())
                                            val viewsports = ViewSports()
                                            viewsports.arguments = bundle

                                            activity.supportFragmentManager.beginTransaction().replace(R.id.rootConstraintLayout, viewsports).commit()
                                        }

                                        override fun onError(anError: ANError) {
                                            done.progress = -1
                                        }
                                    })
                        }

                        override fun onError(anError: ANError) {
                            done.progress = -1
                        }
                    })
        }

        return view
    }
}
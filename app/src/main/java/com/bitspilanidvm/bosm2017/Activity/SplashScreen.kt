package com.bitspilanidvm.bosm2017.Activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.bitspilanidvm.bosm2017.Firebase.FirebaseFetcher
import com.bitspilanidvm.bosm2017.Modals.FixtureSportsData
import com.bitspilanidvm.bosm2017.Modals.Sports
import com.bitspilanidvm.bosm2017.R
import com.bitspilanidvm.bosm2017.Universal.*
import com.crashlytics.android.Crashlytics
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import io.fabric.sdk.android.Fabric
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList



class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Fabric.with(this, Crashlytics())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        val mDatabase = Utils.database.reference.child("Schedule")

        FirebaseFetcher.initialize()

        mDatabase.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                GLOBAL_DATA.sports = Sports()
                GLOBAL_DATA.availableSchedule.clear()
                GLOBAL_DATA.availableResults.clear()

                FirebaseFetcher.fetchAndStore(dataSnapshot)
                getAvailableSchedulesAndResults()
                sortAvailableSchedulesAndResults()
                setUpHeadingsAndDetails()
                fillAvailableOngoingSchedule()
                fillOngoingList()

                startActivity(Intent(this@SplashScreen, Main::class.java))

            }
        })

        val sharedPreferences = applicationContext.getSharedPreferences("Favourites", Context.MODE_PRIVATE)

        val firstTime = sharedPreferences.getBoolean("firstTime", true)
        if (firstTime){

            FirebaseMessaging.getInstance().subscribeToTopic("news")

            val selected = arrayOf("Hockey", "Athletics (Boys)", "Athletics (Girls)", "Basketball (Boys)", "Lawn Tennis (Girls)", "Lawn Tennis (Boys)", "Squash", "Swimming (Boys)", "Football (Boys)", "Badminton (Boys)", "Powerlifting", "Chess", "Table Tennis (Boys)", "Table Tennis (Girls)", "Taekwondo (Boys)", "Taekwondo (Girls)", "Volleyball (Boys)", "Volleyball (Girls)", "Badminton (Girls)", "Carrom", "Swimming (Girls)", "Cricket", "Football (Girls)", "Basketball (Girls)", "Pool")
            val sendString = arrayOf("Hockey", "Athletics_Boys", "Athletics_Girls", "Basketball_Boys", "Lawn_Tennis_Girls", "Lawn_Tennis_Boys", "Squash", "Swimming_Boys", "Football_Boys", "Badminton_Boys", "Powerlifting", "Chess", "Table_Tennis_Boys", "Table_Tennis_Girls", "Taekwondo_Boys", "Taekwondo_Girls", "Volleyball_Boys", "Volleyball_Girls", "Badminton_Girls", "Carrom", "Swimming_Girls", "Cricket", "Football_Girls", "Basketball_Girls", "Pool")

            var s = ""
            for (i in selected)
                s += "$i,"

                s = s.substring(0, s.length - 1)

            for (topic in sendString)
                FirebaseMessaging.getInstance().subscribeToTopic(topic)

            sharedPreferences.edit().putString("selected", s).apply()
            sharedPreferences.edit().putBoolean("firstTime", false).apply()
        }
    }

    fun setUpHeadingsAndDetails(){

        GLOBAL_DATA.headingsSchedule.clear()
        GLOBAL_DATA.headingsResults.clear()
        GLOBAL_DATA.detailsResults.clear()
        GLOBAL_DATA.detailsSchedule.clear()


        var index = 0
        val df = SimpleDateFormat("dd MMM, hh:mm a")

        for (i in GLOBAL_DATA.availableSchedule) {
            GLOBAL_DATA.headingsSchedule.add(GLOBAL_DATA.sportsMap[i] ?: "Not Available")
            GLOBAL_DATA.detailsSchedule.add("Last Updated at ${df.format((GLOBAL_DATA.availableScheduleMap[i] ?: Date()))}")
            index++
        }

        index = 0
        for (i in GLOBAL_DATA.availableResults) {
            GLOBAL_DATA.headingsResults.add(GLOBAL_DATA.sportsMap[i] ?: "Not Available")
            GLOBAL_DATA.detailsResults.add("Last Updated at ${df.format((GLOBAL_DATA.availableResultsMap[i] ?: Date()))}")
            index++
        }
    }

    fun getAvailableSchedulesAndResults(){
        for (i in 0..26) {
            if (GLOBAL_DATA.sports.fixtureSportsDataList[i].isNotEmpty()) {
                GLOBAL_DATA.availableSchedule.add(i)
                if (getWinnerListFromFixtureSportsDataList(GLOBAL_DATA.sports.fixtureSportsDataList[i]).isNotEmpty())
                    GLOBAL_DATA.availableResults.add(i)
            }

            if (GLOBAL_DATA.sports.nonFixtureSportsDataList[i].isNotEmpty()) {
                GLOBAL_DATA.availableSchedule.add(i)
                if (getWinnerListFromNonFixtureSportsDataDecoupledList(convertListToNonFixtureSportsDecoupledList(GLOBAL_DATA.sports.nonFixtureSportsDataList[i])).isNotEmpty())
                    GLOBAL_DATA.availableResults.add(i)
            }
        }
    }

    fun sortAvailableSchedulesAndResults(){

        GLOBAL_DATA.availableSchedule.forEach { i -> GLOBAL_DATA.availableScheduleMap.put(i, getLatestUpdateScheduleDate(i))}
        GLOBAL_DATA.availableResults.forEach { i -> GLOBAL_DATA.availableResultsMap.put(i, getLatestUpdateResultDate(i))}

        Collections.sort(GLOBAL_DATA.availableSchedule, kotlin.Comparator { first, second ->
            return@Comparator GLOBAL_DATA.availableScheduleMap[second]?.compareTo(GLOBAL_DATA.availableScheduleMap[first]) ?: 0
        })

        Collections.sort(GLOBAL_DATA.availableResults, kotlin.Comparator { first, second ->
            return@Comparator GLOBAL_DATA.availableResultsMap[second]?.compareTo(GLOBAL_DATA.availableResultsMap[first]) ?: 0
        })
    }

    fun getLatestUpdateScheduleDate(i: Int): Date{
        if (i in GLOBAL_DATA.fixtures){
            var latestDate: Date? = null
            for (j in GLOBAL_DATA.sports.fixtureSportsDataList[i]) {
                if (latestDate == null)
                    latestDate = j.scheduleTime
                else if (j.scheduleTime > latestDate)
                    latestDate = j.scheduleTime
            }
            return latestDate ?: Date()
        }else{
            var latestDate: Date? = null
            for (j in GLOBAL_DATA.sports.nonFixtureSportsDataList[i]) {
                if (latestDate == null)
                    latestDate = j.scheduleTime
                else if (j.scheduleTime > latestDate)
                    latestDate = j.scheduleTime
            }
            return latestDate ?: Date()
        }
    }

    fun getLatestUpdateResultDate(i: Int): Date{
        if (i in GLOBAL_DATA.fixtures){
            var latestDate: Date? = null
            for (j in getWinnerListFromFixtureSportsDataList(GLOBAL_DATA.sports.fixtureSportsDataList[i])) {
                Log.e("$i", j.resultTime.toString())
                if (latestDate == null)
                    latestDate = j.resultTime
                else if (j.resultTime > latestDate)
                    latestDate = j.resultTime
            }
            return latestDate ?: Date()
        }else{
            var latestDate: Date? = null
            for (j in getWinnerListFromNonFixtureSportsDataDecoupledList(convertListToNonFixtureSportsDecoupledList(GLOBAL_DATA.sports.nonFixtureSportsDataList[i]))) {
                if (latestDate == null)
                    latestDate = j.resultTime
                else if (j.resultTime > latestDate)
                    latestDate = j.resultTime
            }
            return latestDate ?: Date()
        }
    }

    fun fillAvailableOngoingSchedule(){
        GLOBAL_DATA.ongoingFixturesMap.clear()
        GLOBAL_DATA.ongoingNonFixturesMap.clear()
        for (i in GLOBAL_DATA.availableSchedule)
            fillOngoingDetailsFor(i)
    }

    fun fillOngoingDetailsFor(i: Int){
        val dateFormat = SimpleDateFormat("MMM dd yyyy HH:mm:ss")

        if (i in GLOBAL_DATA.fixtures) {
            val results = getWinnerListFromFixtureSportsDataList(GLOBAL_DATA.sports.fixtureSportsDataList[i])
            val list = ArrayList<FixtureSportsData>()
            for (j in GLOBAL_DATA.sports.fixtureSportsDataList[i]) {
                val date = dateFormat.parse("${j.date} ${j.time}")
                if ((System.currentTimeMillis() - date.time < GLOBAL_DATA.ongoingHour) && (System.currentTimeMillis() - date.time >= 0) && (j !in results)) {
                    list.add(j)
                }
            }

            if (list.isNotEmpty()) {
                GLOBAL_DATA.ongoingFixturesMap.put(GLOBAL_DATA.sportsMap[i] ?: "", list)
            }
        }
        else{
            val decoupledList = convertListToNonFixtureSportsDecoupledList(GLOBAL_DATA.sports.nonFixtureSportsDataList[i])
            val results = getWinnerListFromNonFixtureSportsDataDecoupledList(decoupledList)
            val list = ArrayList<NonFixtureSportsDataDecoupled>()
            for (j in decoupledList) {
                val date = dateFormat.parse("${j.date} ${j.time}")
                if ((System.currentTimeMillis() - date.time < GLOBAL_DATA.ongoingHour)  && (System.currentTimeMillis() - date.time >= 0) && (j !in results))
                    list.add(j)
            }

            if (list.isNotEmpty())
                GLOBAL_DATA.ongoingNonFixturesMap.put(GLOBAL_DATA.sportsMap[i] ?: "", list)
        }
    }

    fun fillOngoingList(){
        GLOBAL_DATA.ongoing.clear()

        for ((k, v) in GLOBAL_DATA.ongoingFixturesMap)
            GLOBAL_DATA.ongoing.add(k)

        for ((k, v) in GLOBAL_DATA.ongoingNonFixturesMap)
            GLOBAL_DATA.ongoing.add(k)

        Collections.sort(GLOBAL_DATA.ongoing)
    }
}

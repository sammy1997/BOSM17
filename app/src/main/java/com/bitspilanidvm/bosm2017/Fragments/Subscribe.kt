package com.bitspilanidvm.bosm2017.Fragments

import android.content.Context
import android.graphics.Typeface
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
import com.bitspilanidvm.bosm2017.Activity.Main
import com.bitspilanidvm.bosm2017.Adapters.SportAdapter
import com.bitspilanidvm.bosm2017.ClickListeners.SubscriptionRecyclerViewOnClickListener
import com.bitspilanidvm.bosm2017.R
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class Subscribe : Fragment(){

    lateinit var hamburgerIcon: ImageView
    lateinit var mRecyclerView: RecyclerView

    val favouriteList = ArrayList<String>()
    val sports = arrayOf("Hockey", "Athletics (Boys)", "Athletics (Girls)", "Basketball (Boys)", "Lawn Tennis (Girls)", "Lawn Tennis (Boys)", "Squash", "Swimming (Boys)", "Football (Boys)", "Badminton (Boys)", "Powerlifting", "Chess", "Table Tennis (Boys)", "Table Tennis (Girls)", "Taekwondo (Boys)", "Taekwondo (Girls)", "Volleyball (Boys)", "Volleyball (Girls)", "Badminton (Girls)", "Carrom", "Swimming (Girls)", "Cricket", "Football (Girls)", "Basketball (Girls)", "Pool")
    val sendString = arrayOf("Hockey", "Athletics_Boys", "Athletics_Girls", "Basketball_Boys", "Lawn_Tennis_Girls", "Lawn_Tennis_Boys", "Squash", "Swimming_Boys", "Football_Boys", "Badminton_Boys", "Powerlifting", "Chess", "Table_Tennis_Boys", "Table_Tennis_Girls", "Taekwondo_Boys", "Taekwondo_Girls", "Volleyball_Boys", "Volleyball_Girls", "Badminton_Girls", "Carrom", "Swimming_Girls", "Cricket", "Football_Girls", "Basketball_Girls", "Pool")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_subscribe, container, false)

        hamburgerIcon = view.findViewById(R.id.hamburgerIcon)
        mRecyclerView = view.findViewById(R.id.recycler_view)

        hamburgerIcon.setOnClickListener {
            if (activity.drawerLayout.isDrawerOpen(GravityCompat.START))
                activity.drawerLayout.closeDrawer(GravityCompat.START)
            else
                activity.drawerLayout.openDrawer(GravityCompat.START)
        }

        //bosmText.typeface = Typeface.createFromAsset(activity.assets, "fonts/Ikaros-Regular.otf")

        val sharedPreferences = activity.applicationContext.getSharedPreferences("Favourites", Context.MODE_PRIVATE)
        val subscribedList = sharedPreferences.getString("selected", "")
        val alreadySubscribedTo = subscribedList.split(",")

        if (alreadySubscribedTo.isNotEmpty())
            for (subscription in alreadySubscribedTo)
                favouriteList.add(subscription)

        val typeface = Typeface.createFromAsset(activity.assets, "fonts/Coves-Bold.otf")
        val adapter = SportAdapter(sports, SubscriptionRecyclerViewOnClickListener { holder, position ->
            Log.e("isSelected", holder.shineButton.isChecked.toString())
            if (holder.shineButton.isChecked) {
                favouriteList.add(sports[position])
                Log.e("list", favouriteList.toString())
                FirebaseMessaging.getInstance().subscribeToTopic(sendString[position])
                Snackbar.make(view, "Subscribed to topic " + sports[position], Snackbar.LENGTH_SHORT).show()
            } else {
                if (favouriteList.contains(sports[position])) {
                    favouriteList.remove(sports[position])
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(sendString[position])
                    Snackbar.make(view, "Unsubscribed from topic " + sports[position], Snackbar.LENGTH_SHORT).show()
                }
            }

            var s = ""
            for (i in favouriteList)
                s += "$i,"


            if (s.isNotEmpty())
                s = s.substring(0, s.length - 1)

            Log.e("finalStringTo", s)
            sharedPreferences.edit().putString("selected", s).apply()
        }, favouriteList, typeface, (activity as Main).displayMetrics.density)

        mRecyclerView.adapter = adapter
        mRecyclerView.layoutManager = LinearLayoutManager(activity)
        mRecyclerView.scrollToPosition(20)
        mRecyclerView.scrollToPosition(0)
        return view
    }
}
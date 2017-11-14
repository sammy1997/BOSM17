package com.bitspilanidvm.bosm2017.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bitspilanidvm.bosm2017.Adapters.SportsAdded
import com.bitspilanidvm.bosm2017.R
import com.dd.processbutton.iml.ActionProcessButton
import kotlinx.android.synthetic.main.activity_main.*

class ViewParticipants : Fragment(){

    lateinit var hamburgerIcon: ImageView
    lateinit var recyclerView: RecyclerView
    lateinit var home: ActionProcessButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_view_participants, container, false)
        hamburgerIcon = view.findViewById(R.id.hamburgerIcon)
        recyclerView = view.findViewById(R.id.participants)
        home = view.findViewById(R.id.home)

        hamburgerIcon.setOnClickListener {
            if (activity.drawerLayout.isDrawerOpen(GravityCompat.START))
                activity.drawerLayout.closeDrawer(GravityCompat.START)
            else
                activity.drawerLayout.openDrawer(GravityCompat.START)
        }

        val participants = arguments.getStringArrayList("participants")

        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = SportsAdded(participants)

        home.setOnClickListener {
            activity.supportFragmentManager.beginTransaction().replace(R.id.rootConstraintLayout, ManageSports()).commit()
        }

        return view
    }
}
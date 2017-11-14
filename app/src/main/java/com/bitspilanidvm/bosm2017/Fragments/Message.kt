package com.bitspilanidvm.bosm2017.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bitspilanidvm.bosm2017.R
import kotlinx.android.synthetic.main.activity_main.*

class Message : Fragment(){

    lateinit var message: TextView
    lateinit var hamburgerIcon: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_message, container, false)
        message = view.findViewById(R.id.message)
        hamburgerIcon = view.findViewById(R.id.hamburgerIcon)

        hamburgerIcon.setOnClickListener {
            if (activity.drawerLayout.isDrawerOpen(GravityCompat.START))
                activity.drawerLayout.closeDrawer(GravityCompat.START)
            else
                activity.drawerLayout.openDrawer(GravityCompat.START)
        }


        try {
            message.text = arguments.getString("fragment_message")
        }catch (e: Exception){
            e.printStackTrace()
        }

        return view
    }
}
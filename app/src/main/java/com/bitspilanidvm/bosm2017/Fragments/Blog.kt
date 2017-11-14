package com.bitspilanidvm.bosm2017.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bitspilanidvm.bosm2017.Activity.Main
import com.bitspilanidvm.bosm2017.R
import kotlinx.android.synthetic.main.activity_main.*

class Blog : Fragment(){

    lateinit var HPC: Button
    lateinit var EPC: Button
    lateinit var hamburgerIcon: ImageView
    lateinit var bosmTextView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_decision, container, false)
        HPC = view.findViewById(R.id.viewAddedPlayers)
        EPC = view.findViewById(R.id.addAnother)
        hamburgerIcon = view.findViewById(R.id.hamburgerIcon)
        bosmTextView = view.findViewById(R.id.bosmTextView)
        bosmTextView.text = "BLOG"

        hamburgerIcon.setOnClickListener {
            if (activity.drawerLayout.isDrawerOpen(GravityCompat.START))
                activity.drawerLayout.closeDrawer(GravityCompat.START)
            else
                activity.drawerLayout.openDrawer(GravityCompat.START)
        }

        HPC.text = "HPC\n(Hindi Press Club)"
        EPC.text = "EPC\n(English Press Club)"

        HPC.setOnClickListener { changeFragment("https://hindipressclub.wordpress.com", "HPC Blog") }
        EPC.setOnClickListener { changeFragment("https://epcbits.wordpress.com/category/bosm/bosm-2017/", "EPC Blog") }

        return view
    }

    private fun changeFragment(url: String, heading: String){
        (activity as Main).shouldBeHandledBySystem = true
        val fragment = BlogView()
        val transaction = activity.supportFragmentManager.beginTransaction()
        val bundle = Bundle()
        bundle.putString("URL", url)
        bundle.putString("heading", heading)
        fragment.arguments = bundle
        transaction.replace(R.id.rootConstraintLayout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
package com.bitspilanidvm.bosm2017.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import com.bitspilanidvm.bosm2017.Adapters.DevelopersViewPager
import com.bitspilanidvm.bosm2017.R
import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager
import kotlinx.android.synthetic.main.activity_main.*

class Developers : Fragment(){

    lateinit var horizontalViewPager: HorizontalInfiniteCycleViewPager
    lateinit var hamburgerIcon: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_developers, container, false)

        horizontalViewPager = view.findViewById(R.id.horizontalViewPager)
        hamburgerIcon = view.findViewById(R.id.hamburgerIcon)

        hamburgerIcon.setOnClickListener {
            if (activity.drawerLayout.isDrawerOpen(GravityCompat.START))
                activity.drawerLayout.closeDrawer(GravityCompat.START)
            else
                activity.drawerLayout.openDrawer(GravityCompat.START)
        }

        horizontalViewPager.adapter = DevelopersViewPager(activity)
        horizontalViewPager.interpolator = AccelerateDecelerateInterpolator()
        horizontalViewPager.scrollDuration = 500
        horizontalViewPager.isMediumScaled = true
        horizontalViewPager.maxPageScale = 0.8f
        horizontalViewPager.minPageScale = 0.5f
        horizontalViewPager.centerPageScaleOffset = 30f
        horizontalViewPager.minPageScaleOffset = 5f

        return view
    }
}
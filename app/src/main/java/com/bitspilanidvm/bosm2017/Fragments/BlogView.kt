package com.bitspilanidvm.bosm2017.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import com.bitspilanidvm.bosm2017.R
import kotlinx.android.synthetic.main.activity_main.*

class BlogView : Fragment(){

    lateinit var hamburgerIcon: ImageView
    lateinit var webview: WebView
    lateinit var heading: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_blog_view, container, false)
        hamburgerIcon = view.findViewById(R.id.hamburgerIcon)
        webview = view.findViewById(R.id.webview)
        heading = view.findViewById(R.id.bosmTextView)

        webview.loadUrl(arguments.getString("URL"))
        heading.text = arguments.getString("heading")

        hamburgerIcon.setOnClickListener {
            if (activity.drawerLayout.isDrawerOpen(GravityCompat.START))
                activity.drawerLayout.closeDrawer(GravityCompat.START)
            else
                activity.drawerLayout.openDrawer(GravityCompat.START)
        }
        return view
    }
}
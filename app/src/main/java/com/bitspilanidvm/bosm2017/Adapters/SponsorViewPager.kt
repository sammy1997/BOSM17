package com.bitspilanidvm.bosm2017.Adapters

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bitspilanidvm.bosm2017.R
import com.bitspilanidvm.bosm2017.Universal.GLOBAL_DATA
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator

class SponsorViewPager(val context: Context) : PagerAdapter(){

    val typeface = Typeface.createFromAsset(context.assets, "fonts/Coves-Bold.otf")
    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        val itemView = LayoutInflater.from(context).inflate(R.layout.sponsor_item_layout, container, false)

        val image = itemView.findViewById<ImageView>(R.id.image)
        val text = itemView.findViewById<TextView>(R.id.text)
        val type = itemView.findViewById<TextView>(R.id.description)

        picasso(context, GLOBAL_DATA.sponsorImageRes[position]).into(image)
        text.text = GLOBAL_DATA.sponsorText[position]
        type.text = GLOBAL_DATA.typeText[position]
        if (GLOBAL_DATA.sponsorText[position] == "ONGC" || GLOBAL_DATA.sponsorText[position] == "Panasonic") {
            type.textSize = 30f
            type.typeface = Typeface.DEFAULT_BOLD
        }

        if (GLOBAL_DATA.sponsorText[position] == "Lucky Star")
            image.setBackgroundColor(Color.GRAY)

        text.typeface = typeface
        type.typeface = typeface

        container?.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        container?.removeView(`object` as CardView)
    }

    override fun isViewFromObject(view: View?, `object`: Any?) = view == `object` as CardView

    override fun getCount() = GLOBAL_DATA.sponsorImageRes.size

    fun picasso(context: Context, resourceId: Int): RequestCreator {
        return Picasso.with(context)
                .load(resourceId)
                .fit()
                .centerInside()
    }
}
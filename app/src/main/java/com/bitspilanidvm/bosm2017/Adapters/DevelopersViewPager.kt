package com.bitspilanidvm.bosm2017.Adapters

import android.content.Context
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

class DevelopersViewPager(val context: Context) : PagerAdapter(){

    val typeface = Typeface.createFromAsset(context.assets, "fonts/Coves-Bold.otf")
    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        val itemView = LayoutInflater.from(context).inflate(R.layout.developer_item_layout, container, false)

        val image = itemView.findViewById<ImageView>(R.id.image)
        val name = itemView.findViewById<TextView>(R.id.name)
        val description = itemView.findViewById<TextView>(R.id.description)

        picasso(context, GLOBAL_DATA.developerImageRes[position]).into(image)
        name.text = GLOBAL_DATA.developerName[position]
        description.text = GLOBAL_DATA.developerDescription[position]
        name.typeface = typeface
        description.typeface = typeface

        container?.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        container?.removeView(`object` as CardView)
    }

    override fun isViewFromObject(view: View?, `object`: Any?) = view == `object` as CardView

    override fun getCount() = GLOBAL_DATA.developerImageRes.size

    fun picasso(context: Context, resourceId: Int): RequestCreator {
        return Picasso.with(context)
                .load(resourceId)
                .fit()
                .centerCrop()
    }
}
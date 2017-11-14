package com.bitspilanidvm.bosm2017.Adapters

import android.content.Context
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bitspilanidvm.bosm2017.Universal.GLOBAL_DATA
import com.bitspilanidvm.bosm2017.R
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator

class HeaderViewPager(val context: Context) : PagerAdapter() {

    var pageMap = HashMap<Int, TextView>()
    var pages = HashMap<Int, View>()

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        val itemView = LayoutInflater.from(context).inflate(R.layout.header_view_pager_page, container, false)
        val imageView = itemView.findViewById<ImageView>(R.id.headerPagerImageView)
        val textView = itemView.findViewById<TextView>(R.id.itemText)
        pageMap.put(position, textView)
        pages.put(position, itemView)
        //imageView.setBackgroundResource(GLOBAL_DATA.imageDrawableRes[position])
        picasso(context, GLOBAL_DATA.imageDrawableRes[position]).into(imageView)
        textView.text = GLOBAL_DATA.headerTitles[position]
        textView.scaleX = GLOBAL_DATA.textScale
        textView.scaleY = GLOBAL_DATA.textScale
        textView.typeface = Typeface.createFromAsset(context.assets, "fonts/Ikaros-Regular.otf")
        textView.setShadowLayer(2f, 2f, 2f, ContextCompat.getColor(context, GLOBAL_DATA.shadowColors[position]))
        container?.addView(itemView)
        return itemView
    }
    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        container?.removeView(`object` as FrameLayout)
        pageMap.remove(position)
    }

    override fun isViewFromObject(view: View?, `object`: Any?) = view == `object` as FrameLayout

    override fun getCount() = GLOBAL_DATA.headerTitles.size

    fun picasso(context: Context, resourceId: Int): RequestCreator {
        return Picasso.with(context)
                .load(resourceId)
                .fit()
    }
}

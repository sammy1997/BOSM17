package com.bitspilanidvm.bosm2017.Custom

import android.support.v4.view.ViewPager
import android.view.View

class Transformer_HeaderPage : ViewPager.PageTransformer{

    override fun transformPage(page: View?, position: Float) {
        page?.setTranslationX(page?.getWidth() * -position)

        if(position <= -1.0F || position >= 1.0F) {
            page?.setAlpha(0.0F);
        } else if( position == 0.0F ) {
            page?.setAlpha(1.0F);
        } else {
            // position is between -1.0F & 0.0F OR 0.0F & 1.0F
            page?.setAlpha(1.0F - Math.abs(position))
        }
    }
}
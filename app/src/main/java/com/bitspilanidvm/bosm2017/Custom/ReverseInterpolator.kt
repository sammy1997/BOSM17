package com.bitspilanidvm.bosm2017.Custom

import android.view.animation.DecelerateInterpolator

class ReverseInterpolator : DecelerateInterpolator() {
    override fun getInterpolation(paramFloat: Float): Float {
        return Math.abs(super.getInterpolation(paramFloat) - 1f)
    }
}
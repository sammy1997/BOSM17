package com.bitspilanidvm.bosm2017.Custom;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomPager extends ViewPager {
    CustomPager mCustomPager;
    private boolean forSuper;

    public CustomPager(Context context) {
        super(context);
    }

    public CustomPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (!forSuper) {

            mCustomPager.forSuper(true);
            mCustomPager.onInterceptTouchEvent(arg0);
            mCustomPager.forSuper(false);
        }
        return super.onInterceptTouchEvent(arg0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (!forSuper) {
            mCustomPager.forSuper(true);
            mCustomPager.onTouchEvent(arg0);
            mCustomPager.forSuper(false);
        }
        return super.onTouchEvent(arg0);
    }

    public void setViewPager(CustomPager customPager) {
        mCustomPager = customPager;
    }

    public void forSuper(boolean forSuper) {
        this.forSuper = forSuper;
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        if (!forSuper) {
            mCustomPager.forSuper(true);
            mCustomPager.setCurrentItem(item, smoothScroll);
            mCustomPager.forSuper(false);
        }
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        if (!forSuper) {
            mCustomPager.forSuper(true);
            mCustomPager.setCurrentItem(item);
            mCustomPager.forSuper(false);
        }
        super.setCurrentItem(item);

    }

}
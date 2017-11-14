package com.bitspilanidvm.bosm2017.Custom;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public class CustomAnimator extends AnimationAdapter {

    public CustomAnimator(RecyclerView.Adapter adapter) {
        super(adapter);
    }

    @Override protected Animator[] getAnimators(View view) {
        return new Animator[] {
                ObjectAnimator.ofFloat(view, "translationX", view.getRootView().getWidth(), 0),
                ObjectAnimator.ofFloat(view, "alpha", 0f, 1f),
                ObjectAnimator.ofFloat(view, "scaleX", .1f, 1f),
                ObjectAnimator.ofFloat(view, "scaleY", .1f, 1f)
        };
    }
}

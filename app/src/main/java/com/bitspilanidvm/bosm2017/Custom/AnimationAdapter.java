package com.bitspilanidvm.bosm2017.Custom;

import android.animation.Animator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import jp.wasabeef.recyclerview.internal.ViewHelper;


public abstract class AnimationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private RecyclerView.Adapter<RecyclerView.ViewHolder> mAdapter;
  private int mDuration = 300;
  private Interpolator mInterpolator = new DecelerateInterpolator();
  private int mLastPosition = -1;

  private boolean isFirstOnly = true;

  public AnimationAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
    mAdapter = adapter;
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return mAdapter.onCreateViewHolder(parent, viewType);
  }

  @Override public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
    super.registerAdapterDataObserver(observer);
    mAdapter.registerAdapterDataObserver(observer);
  }

  @Override public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
    super.unregisterAdapterDataObserver(observer);
    mAdapter.unregisterAdapterDataObserver(observer);
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    mAdapter.onBindViewHolder(holder, position);

    int adapterPosition = holder.getAdapterPosition();
    if (!isFirstOnly || adapterPosition > mLastPosition) {
      for (Animator anim : getAnimators(holder.itemView)) {
        anim.setDuration(mDuration).start();
        anim.setInterpolator(mInterpolator);
      }
      mLastPosition = adapterPosition;
    } else {
      ViewHelper.clear(holder.itemView);
    }
  }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        for (Animator anim : getAnimators(holder.itemView)) {
            anim.setDuration(mDuration).start();
            anim.setInterpolator(mInterpolator);
        }

    }

    @Override public void onViewRecycled(RecyclerView.ViewHolder holder) {
    mAdapter.onViewRecycled(holder);
    super.onViewRecycled(holder);
  }

  @Override public int getItemCount() {
    return mAdapter.getItemCount();
  }

  protected abstract Animator[] getAnimators(View view);

  @Override public int getItemViewType(int position) {
    return mAdapter.getItemViewType(position);
  }


  @Override public long getItemId(int position) {
    return mAdapter.getItemId(position);
  }
}

package com.bitspilanidvm.bosm2017.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitspilanidvm.bosm2017.ClickListeners.SubscriptionRecyclerViewOnClickListener;
import com.bitspilanidvm.bosm2017.R;
import com.bitspilanidvm.bosm2017.Universal.GLOBAL_DATA;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.List;
import java.util.Map;

public class SportAdapter extends RecyclerView.Adapter<SportAdapter.MyViewHolder> {

    private String[] sportList;
    SubscriptionRecyclerViewOnClickListener listener;
    List<String> favouriteList;
    Typeface typeface;
    float density;
    final String[] sports = {"Hockey", "Athletics (Boys)", "Athletics (Girls)", "Basketball (Boys)", "Lawn Tennis (Girls)", "Lawn Tennis (Boys)", "Squash", "Swimming (Boys)", "Football (Boys)", "Badminton (Boys)", "Powerlifting", "Chess", "Table Tennis (Boys)", "Table Tennis (Girls)", "Taekwondo (Boys)", "Taekwondo (Girls)", "Volleyball (Boys)", "Volleyball (Girls)", "Badminton (Girls)", "Carrom", "Swimming (Girls)", "Cricket", "Football (Girls)", "Basketball (Girls)", "Pool"};


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView SportTitle;
        public ShineButton shineButton;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            SportTitle = (TextView) view.findViewById(R.id.title_id);
            shineButton = (ShineButton) view.findViewById(R.id.shineBtn);
            image = view.findViewById(R.id.sportIcon);
        }
    }

    public SportAdapter(String[] sportList, SubscriptionRecyclerViewOnClickListener listener, List<String> favouriteList, Typeface typeface, float density) {
        this.sportList = sportList;
        this.listener = listener;
        this.favouriteList = favouriteList;
        this.typeface = typeface;
        this.density = density;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (favouriteList.contains(sports[position])) {
            holder.shineButton.setChecked(true);
        } else {
            holder.shineButton.setChecked(false);
        }
        holder.SportTitle.setTypeface(typeface);
        holder.SportTitle.setText(sportList[position]);
        Map map = GLOBAL_DATA.INSTANCE.getSportsImageIconRes();
        picasso(holder.image.getContext(), (int) map.get(sportList[position])).into(holder.image);
        //holder.image.setImageResource((int) map.get(sportList[position]));
        holder.shineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(holder, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return sportList.length;
    }

    RequestCreator picasso(Context context, int resourceId) {
        return Picasso.with(context)
                .load(resourceId)
                .fit();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout, parent, false);
        ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        marginLayoutParams.setMargins((int)(4 * density),0,(int)(4 * density),(int)(4 * density));
        v.setLayoutParams(marginLayoutParams);
        return new MyViewHolder(v);
    }
}
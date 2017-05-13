package com.muusa.sweetaroma.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.muusa.sweetaroma.R;


import com.muusa.sweetaroma.model.Slider;
import com.zanlabs.widget.infiniteviewpager.InfinitePagerAdapter;

import java.util.ArrayList;

/**
 * Created by sydney on 9/29/16.
 */

public class SliderAdapter extends InfinitePagerAdapter {

    private final LayoutInflater mInflater;
    private final Context mContext;

    private ArrayList<Slider> mList;

    public SliderAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setDataList(ArrayList<Slider> sliderImages) {
        if (sliderImages == null || sliderImages.size() == 0)
            throw new IllegalArgumentException("list can not be null or has an empty size");
        this.mList = sliderImages;
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View view, ViewGroup container) {
        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = mInflater.inflate(R.layout.item_layout_slider, container, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        Slider slider = mList.get(position);
        Glide.with(mContext)
                .load(slider.getSliderImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.image);
        return view;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    private static class ViewHolder {
        ImageView image;

        ViewHolder(View view) {
            image = (ImageView) view.findViewById(R.id.sliderImage);
        }
    }
}

package com.muusa.sweetaroma.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.muusa.sweetaroma.NearByPlace;
import com.muusa.sweetaroma.R;

import java.util.ArrayList;

/**
 * Created by Muusa on 5/13/2017.
 */

public class NearByPlacesAdapter extends RecyclerView.Adapter<NearByPlacesAdapter.ViewHolder> {

    private ArrayList<NearByPlace> nearByPlaces = new ArrayList<>();
    private Context context;

    public NearByPlacesAdapter(ArrayList<NearByPlace> nearBySuperMarkets, Context context) {
        this.nearByPlaces = nearBySuperMarkets;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_layout_nearbyplaces, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NearByPlace nearByPlace = nearByPlaces.get(position);

        holder.supermarketName.setText(nearByPlace.getPlaceName());
        holder.vicinity.setText(nearByPlace.getVicinity());
        holder.ratings.setRating(nearByPlace.getRating());

        Glide.with(context)
                .load(nearByPlace.getIcon())
                .asBitmap()
                .placeholder(context.getResources().getDrawable(R.mipmap.ic_launcher))
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.supermarketIcon);
    }

    @Override
    public int getItemCount() {
        return null != nearByPlaces ? nearByPlaces.size() : 0;
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final SimpleRatingBar ratings;
        private ImageView supermarketIcon;
        private TextView supermarketName, vicinity, txtDistance;

        ViewHolder(View itemView) {
            super(itemView);

            supermarketIcon = (ImageView) itemView.findViewById(R.id.supermarketIcon);
            supermarketName = (TextView) itemView.findViewById(R.id.supermarketName);
            vicinity = (TextView) itemView.findViewById(R.id.vicinity);
//            txtDistance = (TextView) itemView.findViewById(R.id.txtDistance);
            ratings = (SimpleRatingBar) itemView.findViewById(R.id.ratings);
        }
    }

}

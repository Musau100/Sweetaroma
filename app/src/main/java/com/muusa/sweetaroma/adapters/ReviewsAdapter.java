package com.muusa.sweetaroma.adapters;

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
import com.muusa.sweetaroma.R;
import com.muusa.sweetaroma.models.Reviews;

import java.util.ArrayList;

import static com.muusa.sweetaroma.R.id.reviewName;
import static com.muusa.sweetaroma.R.id.reviewPic;

/**
 * Created by MUUSA on 2/20/2017.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder>  {

    private Context context;
    private ArrayList<Reviews> review=new ArrayList<>();

    public ReviewsAdapter(Context context, ArrayList<Reviews> reviews) {
        this.context = context;
        this.review = reviews;
    }

    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_layout_design, parent, false));
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.ViewHolder holder, int position) {

        Reviews reviews = review.get(position);

        holder.revieName.setText(reviews.getReviewName());
        holder.creationDate.setText("Design created on: "+reviews.getReviewCreationDate());
        Glide.with(context)
                .load(reviews.getReviewImageUrl())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(android.R.drawable.stat_notify_error)
                .into(holder.reviePic);


    }

    @Override
    public int getItemCount() {
        return null != review ? review.size() : 0;
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

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView revieName,creationDate;
        ImageView reviePic,restaurantProfilePic;
        public ViewHolder(View itemView) {
            super(itemView);

            revieName= (TextView) itemView.findViewById(reviewName);
            creationDate= (TextView) itemView.findViewById(R.id.creationDate);
            reviePic=(ImageView) itemView.findViewById(reviewPic);

        }
    }


}

package com.muusa.sweetaroma.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.muusa.sweetaroma.R;
import com.muusa.sweetaroma.adapters.ReviewsAdapter;
import com.muusa.sweetaroma.models.Reviews;

import java.util.ArrayList;

public class ReviewsActivity extends AppCompatActivity {
    private ArrayList<Reviews> reviews = new ArrayList<>();
    ReviewsAdapter reviewsAdapter;
    private RecyclerView reviewRecyclerView;
    private SwipeRefreshLayout mainSwipeRefreshLayout;
    CoordinatorLayout mainCoordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assignViews();

        reviewsAdapter = new ReviewsAdapter(this, reviews);

        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewRecyclerView.setItemAnimator(new DefaultItemAnimator());
        reviewRecyclerView.setAdapter(reviewsAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void assignViews() {

        reviewRecyclerView=(RecyclerView)findViewById(R.id.reviewsRecyclerView);
        mainCoordinatorLayout=(CoordinatorLayout)findViewById(R.id.mainCoordinatorLayout);
        mainSwipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.mainSwipeRefreshLayout);

    }

}

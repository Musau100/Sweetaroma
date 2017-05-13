package com.muusa.sweetaroma.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.github.pierry.simpletoast.SimpleToast;
import com.muusa.sweetaroma.R;
import com.muusa.sweetaroma.adapter.SliderAdapter;
import com.muusa.sweetaroma.utils.App;
import com.muusa.sweetaroma.utils.AppConfig;
import com.muusa.sweetaroma.utils.InternetConnection;
import com.rey.material.widget.Slider;
import com.zanlabs.widget.infiniteviewpager.InfiniteViewPager;
import com.zanlabs.widget.infiniteviewpager.indicator.LinePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class RestaurantProfileActivity extends AppCompatActivity {
    InfiniteViewPager mViewPager;
    LinePageIndicator mLineIndicator;
    private SwipeRefreshLayout mainSwipeRefreshLayout;
    private CoordinatorLayout mainCoordinatorLayout;
    ArrayList<Slider> sliderImages = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        assignViews();

        mainSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                loadData();
            }
        });
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit Application?");
        alertDialogBuilder
                .setMessage("Click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private void loadData() {
        if (new InternetConnection(getBaseContext()).isInternetAvailable()) {


            loadSliderImages();


        } else {
            Toast.makeText(this, "No Internet Conection!", Toast.LENGTH_SHORT).show();
            showWirelessSettings();
        }
    }

    private void loadSliderImages() {

        showRefreshing(true);

        JsonArrayRequest request = new JsonArrayRequest(SLIDER_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray result) {
                showRefreshing(false);

                Log.i(AppConfig.TAG, "onResponse: Slider Result= " + result.toString());
                parseSliderResult(result);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showRefreshing(false);

                Snackbar snackbar = Snackbar
                        .make(mainCoordinatorLayout, "Internet Connection Error", Snackbar.LENGTH_LONG)
                        .setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                loadData();
                            }
                        });
// Changing message text color
                snackbar.setActionTextColor(Color.RED);
                snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);

                // Changing action button text color
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);

                snackbar.show();

                SimpleToast.error(getBaseContext(), "Something Went Wrong While Fetching The Services");
                Log.e(AppConfig.TAG, "onErrorResponse: Error= " + error.getMessage());
            }
        });

        App.getInstance().addToRequestQueue(request);
    }

    private void parseSliderResult(JSONArray result) {
        sliderImages.clear();

        String sliderImageUrl;
        try {
            for (int i = 0; i < result.length(); i++) {

//                caption = category.getString(CAPTION);
                sliderImageUrl = result.getString(i);
//                sliderImageUrl = SLIDER_IMAGE_URL + category.getString(SLIDER_IMAGE).replace(IMAGE_REGEX, ANDROID_FORMAT);

                Log.i(AppConfig.TAG, "parseSliderResult: SliderImageUrl= " + sliderImageUrl);

                sliderImages.add(new Slider(sliderImageUrl));
            }

            SliderAdapter pagerAdapter = new SliderAdapter(this);
            pagerAdapter.setDataList(sliderImages);
            mViewPager.setAdapter(pagerAdapter);
            mViewPager.setAutoScrollTime(3000);
            mViewPager.startAutoScroll();
            mLineIndicator.setViewPager(mViewPager);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(AppConfig.TAG, "parseSliderResult: Error converting json array" + e.getMessage());
        }
    }

    private void showWirelessSettings() {
        Snackbar snackbar = Snackbar
                .make(mainCoordinatorLayout, "Wifi & Data Disabled!", Snackbar.LENGTH_LONG)
                .setAction("Enable", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                    }
                });
// Changing message text color
        snackbar.setActionTextColor(Color.RED);
        snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);

        snackbar.show();
    }
    private void showRefreshing(boolean refreshing) {
        if (refreshing) {
            mainSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mainSwipeRefreshLayout.setRefreshing(true);
                }
            });
        } else {
            if (mainSwipeRefreshLayout.isShown()) {
                mainSwipeRefreshLayout.setRefreshing(false);
            }
        }
    }
    private void assignViews() {
        mViewPager = (InfiniteViewPager) findViewById(R.id.viewpager);
        mLineIndicator = (LinePageIndicator) findViewById(R.id.indicator);
        mainSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.restSwipeRefresh);
        mainCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.restaurantCoordinator);
    }

}

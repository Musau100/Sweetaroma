package com.muusa.sweetaroma.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.muusa.sweetaroma.R;
import com.muusa.sweetaroma.utils.Constants;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

public class SplashScreen extends AwesomeSplash {
    private long SLEEP_TIMEOUT = 1000;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash_screen);
////        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
////        setSupportActionBar(toolbar);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                try {
//                    Thread.sleep(SLEEP_TIMEOUT);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } finally {
//                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
//                    finish();
//                }
//            }
//        }).start();
//
////        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
////        fab.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
////            }
////        });
//    }
@Override
public void initSplash(ConfigSplash configSplash) {
         /* you don't have to override every property */

    //Customize Circular Reveal
    configSplash.setBackgroundColor(R.color.colorPrimary); //any color you want form colors.xml
    configSplash.setAnimCircularRevealDuration(2000); //int ms
    configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
    configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

    //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

    //Customize Logo
    configSplash.setLogoSplash(R.drawable.icon_sweet); //or any other drawable
    configSplash.setAnimLogoSplashDuration(2000); //int ms
    configSplash.setAnimLogoSplashTechnique(Techniques.Bounce); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)


    //Customize Path
    configSplash.setPathSplash(Constants.DROID_LOGO); //set path String
    configSplash.setOriginalHeight(400); //in relation to your svg (path) resource
    configSplash.setOriginalWidth(400); //in relation to your svg (path) resource
    configSplash.setAnimPathStrokeDrawingDuration(3000);
    configSplash.setPathSplashStrokeSize(3); //I advise value be <5
    configSplash.setPathSplashStrokeColor(R.color.colorAccent); //any color you want form colors.xml
    configSplash.setAnimPathFillingDuration(3000);
    configSplash.setPathSplashFillColor(R.color.orange); //path object filling color


    //Customize Title
    configSplash.setTitleSplash("Sweet Aroma Application");
    configSplash.setTitleTextColor(R.color.orange);
    configSplash.setTitleTextSize(30f); //float value
    configSplash.setAnimTitleDuration(3000);
    configSplash.setAnimTitleTechnique(Techniques.FlipInX);
    configSplash.setTitleFont("fonts/diti_sweet.ttf"); //provide string to your font located in assets/fonts/

}

    @Override
    public void animationsFinished() {
        startActivity(new Intent(SplashScreen.this, LoginActivity.class));
        finish();
    }
}

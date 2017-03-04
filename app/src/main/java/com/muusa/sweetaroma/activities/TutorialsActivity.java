package com.muusa.sweetaroma.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.muusa.sweetaroma.R;
import com.pixplicity.easyprefs.library.Prefs;

import static com.muusa.sweetaroma.utils.AppConfig.FIRST_RUN;

public class TutorialsActivity extends AppIntro2 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Prefs.getBoolean(FIRST_RUN, true)) {
//            show tutorial
            startActivity(new Intent(TutorialsActivity.this, SplashScreen.class));
            finish();
        }
        //setContentView(R.layout.activity_custom_typeface);
        addSlide(AppIntroFragment.newInstance("Get all the best soccer games.", "Within a short time", R.drawable.naivasha_hippo, Color.parseColor("#27ae60")));
        addSlide(AppIntroFragment.newInstance("Get a list of all the categories in each country.", "Choose easily from the list' Countries' Categories", R.drawable.samburu_beautiful, Color.parseColor("#34495e")));
        addSlide(AppIntroFragment.newInstance("View all the matches in the countries.", "Have a Glimpse of all matches odds a Country Offers", R.drawable.samburu_beautiful, Color.parseColor("#c0392b")));
        addSlide(AppIntroFragment.newInstance("Fill your details and make the payment.", "Experience This Smart Revolutionary Shopping With Karibu pay in your hands", R.drawable.rwanda_backets, Color.parseColor("#27ae60")));

        setZoomAnimation();
//        setContentView(R.layout.activity_tutorials);



    }
    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        loadMainActivity();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {

    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        loadMainActivity();
    }

    private void loadMainActivity() {
        //        Initialize Tutorial Status
        Prefs.putBoolean(FIRST_RUN, false);

        startActivity(new Intent(TutorialsActivity.this, LoginActivity.class));
        finish();
    }
}

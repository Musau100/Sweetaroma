package com.muusa.sweetaroma.model;

/**
 * Created by Muusa on 5/10/2017.
 */

public class Slider {

    private String sliderImageUrl;

    public Slider() {
    }

    Slider(String sliderImageUrl) {
        this.sliderImageUrl = sliderImageUrl;
    }

    public String getSliderImageUrl() {
        return sliderImageUrl;
    }

    public void setSliderImageUrl(String sliderImageUrl) {
        this.sliderImageUrl = sliderImageUrl;
    }
}

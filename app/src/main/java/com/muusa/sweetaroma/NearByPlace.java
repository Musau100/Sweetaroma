package com.muusa.sweetaroma;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sydney on 4/1/17.
 */

public class NearByPlace implements Parcelable {

    private String id, placeId, placeName, reference, vicinity, openNow,icon;
    private Double latitude,longitude;
    private float rating;

    public NearByPlace() {
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getOpenNow() {
        return openNow;
    }

    public void setOpenNow(String openNow) {
        this.openNow = openNow;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.icon);
        dest.writeString(this.id);
        dest.writeString(this.placeId);
        dest.writeString(this.placeName);
        dest.writeString(this.reference);
        dest.writeString(this.vicinity);
        dest.writeString(this.openNow);
        dest.writeValue(this.latitude);
        dest.writeValue(this.longitude);
        dest.writeFloat(this.rating);
    }

    protected NearByPlace(Parcel in) {
        this.icon =  in.readString();
        this.id = in.readString();
        this.placeId = in.readString();
        this.placeName = in.readString();
        this.reference = in.readString();
        this.vicinity = in.readString();
        this.openNow = in.readString();
        this.latitude = (Double) in.readValue(Double.class.getClassLoader());
        this.longitude = (Double) in.readValue(Double.class.getClassLoader());
        this.rating = in.readFloat();
    }

    public static final Creator<NearByPlace> CREATOR = new Creator<NearByPlace>() {
        @Override
        public NearByPlace createFromParcel(Parcel source) {
            return new NearByPlace(source);
        }

        @Override
        public NearByPlace[] newArray(int size) {
            return new NearByPlace[size];
        }
    };
}

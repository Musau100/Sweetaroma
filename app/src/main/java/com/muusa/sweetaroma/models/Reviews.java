package com.muusa.sweetaroma.models;

/**
 * Created by MUUSA on 2/20/2017.
 */

public class Reviews {

    private String reviewId, reviewName, reviewDescription, reviewImageUrl, reviewCreationDate;

    public Reviews() {
    }

    public Reviews(String reviewId, String reviewName, String reviewDescription, String reviewImageUrl, String reviewCreationDate) {
        this.reviewId = reviewId;
        this.reviewName = reviewName;
        this.reviewDescription = reviewDescription;
        this.reviewImageUrl = reviewImageUrl;
        this.reviewCreationDate = reviewCreationDate;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getReviewName() {
        return reviewName;
    }

    public void setReviewName(String reviewName) {
        this.reviewName = reviewName;
    }

    public String getReviewDescription() {
        return reviewDescription;
    }

    public void setReviewDescription(String reviewDescription) {
        this.reviewDescription = reviewDescription;
    }

    public String getReviewImageUrl() {
        return reviewImageUrl;
    }

    public void setReviewImageUrl(String reviewImageUrl) {
        this.reviewImageUrl = reviewImageUrl;
    }

    public String getReviewCreationDate() {
        return reviewCreationDate;
    }

    public void setReviewCreationDate(String reviewCreationDate) {
        this.reviewCreationDate = reviewCreationDate;
    }
}

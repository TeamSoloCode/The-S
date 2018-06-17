package com.example.bruce.myapp.ApiPostObject;

/**
 * Created by BRUCE on 6/7/2018.
 */

public class PostRating {
    private String userId;
    private String locationId;
    private float rateValue;

    public PostRating() {
    }

    public PostRating(String userId, String locationId, float rateValue) {
        this.userId = userId;
        this.locationId = locationId;
        this.rateValue = rateValue;
    }
}

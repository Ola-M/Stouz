package com.example.stouz;

import java.io.Serializable;

public class Restaurant implements Serializable {
    private String name;
    private String openingHours;
    private double rating;
    private String imageUrl;
    private double latitude;
    private double longitude;
    private float distance; // Distance to the restaurant

    public Restaurant(String name, String openingHours, double rating, String imageUrl, double latitude, double longitude) {
        this.name = name;
        this.openingHours = openingHours;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public double getRating() {
        return rating;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}

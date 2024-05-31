package com.example.stouz;

import java.io.Serializable;

public class Restaurant implements Serializable {
    private String name;
    private String openingHours;
    private double rating;
    private String imageUrl;

    public Restaurant(String name, String openingHours, double rating, String imageUrl) {
        this.name = name;
        this.openingHours = openingHours;
        this.rating = rating;
        this.imageUrl = imageUrl;
    }

    // Getters
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

    // Setters
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
}

package com.example.stouz.models;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Restaurant implements Serializable {
    private String id;
    private String name;
    private String openingHours;
    private double avgRating;
    private String imageUrl;
    private double latitude;
    private double longitude;
    private float distance;
    private List<Comment> commentList;
    private RestaurantMenu menu;
    private List<String> userFavorites;
    private int views;

    public Restaurant() {}

    public Restaurant(String id, String name, String openingHours, String imageUrl, float latitude, float longitude, List<Comment> commentList, RestaurantMenu menu, List<String> userFavorites, int views) {
        this.id = id;
        this.name = name;
        this.openingHours = openingHours;
        this.imageUrl = imageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.commentList = (commentList != null) ? commentList : new ArrayList<>();

        this.menu = menu;
        this.userFavorites = userFavorites;
        this.views = views;
    }

    // Getters and Setters

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public double getAvgRating() {
        if(this.getCommentList().size() == 0)
            return 0;

        int temp = 0;
        for (Comment comment:this.getCommentList()) {
            temp += comment.getRate();
        }

        return temp / this.getCommentList().size();
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

    public List<Comment> getCommentList() {
        return commentList;
    }

    public RestaurantMenu getMenu() {
        return menu;
    }

    public List<String> getUserFavorites() {
        return userFavorites;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
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

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public void setMenu(RestaurantMenu menu) {
        this.menu = menu;
    }

    public void setUserFavorites(List<String> userFavorites) {
        this.userFavorites = userFavorites;
    }
}

package com.example.stouz.models;

import java.io.Serializable;
import java.util.List;

public class Restaurant implements Serializable {
    private String id;
    private String name;
    private String openingHours;
    private double avgRating;
    private String imageUrl;
    private double latitude;
    private double longitude;
    private float distance; // Distance to the restaurant
    private List<Comment> commentList;
    private RestaurantMenu menu;
    private List<String> userFavorites;

    public Restaurant(){}
    public Restaurant(String id, String name, String openingHours, double avgRating, String imageUrl, double latitude, double longitude, List<Comment> commentList, RestaurantMenu menu, List<String> userFavorites) {
        this.id = id;
        this.name = name;
        this.openingHours = openingHours;
        this.avgRating = avgRating;
        this.imageUrl = imageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.commentList = commentList;
        this.menu = menu;
        this.userFavorites = userFavorites;
    }

    // Getters and Setters

    public String getId() {return id;}
    public String getName() {
        return name;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public double getAvgRating() {
        return avgRating;
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
    public RestaurantMenu getMenu() {return menu;}
    public List<String> getUserFavorites() {return userFavorites;}


    public void setDistance(float distance) {
        this.distance = distance;
    }
    public void setId (String id) {this.id = id;}
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
    public void setCommentList(Comment comment) {
         this.commentList.add(comment);
    }
    public void setMenu(RestaurantMenu menu){this.menu = menu;}
    public void setUserFavorites(String userFavorite) {this.userFavorites.add(userFavorite);}
}

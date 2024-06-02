package com.example.stouz.models;

import java.util.List;

public class Comment {
    private String id;
    private String user;
    private String description;
    private int rate;
    private String imageUrl;
    private double price;

    public Comment(){}
    public Comment(String id, String user, String description, int rate, String imageUrl, double price){
        this.id = id;
        this.user = user;
        this.description = description;
        this.rate = rate;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public String getId() {
        return id;
    }
    public String getUser() {
        return userId;
    }
    public String getDescription() {
        return description;
    }
    public int rate() {
        return rate;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public double getPrice() {
        return price;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setRate(int rate) {
        this.rate = rate;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public void setPrice(double price) {
        this.price = price;
    }
}



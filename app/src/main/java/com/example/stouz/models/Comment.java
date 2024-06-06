package com.example.stouz.models;

public class Comment {
    private String id;
    private String user;
    private String description;
    private int rate;
    private String imageUrl;

    public Comment() {}

    public Comment(String id, String user, String description, int rate, String imageUrl) {
        this.id = id;
        this.user = user;
        this.description = description;
        this.rate = rate;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public String getDescription() {
        return description;
    }

    public int getRate() {
        return rate;
    }

    public String getImageUrl() {
        return imageUrl;
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
}

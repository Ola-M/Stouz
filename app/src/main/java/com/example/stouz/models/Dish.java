package com.example.stouz.models;

public class Dish {
    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private double price;

    public Dish(){}
    public Dish(String id, String name, String description, String imageUrl, double price){
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public String getId() {return id;}
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() { return imageUrl;}

    public void setId(String id) {this.id = id;}
    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}


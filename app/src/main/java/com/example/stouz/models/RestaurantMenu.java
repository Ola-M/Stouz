package com.example.stouz.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RestaurantMenu implements Serializable {
    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private List<DishCategory> categories;

    public RestaurantMenu() {}
    public RestaurantMenu(String id, String name, String description, String imageUrl, List<DishCategory> categories) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.categories = categories;
    }

    // Getters
    public String getId() {return id;}
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<DishCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<DishCategory> categories) {
        this.categories = categories;
    }

    // Setters
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
}

package com.example.stouz.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RestaurantMenu implements Serializable {
    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private List<Dish> dishes;

    public RestaurantMenu() {}
    public RestaurantMenu(String id, String name, String description, String imageUrl, List<Dish> dishes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.dishes = dishes;
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
    public List<Dish> getDishes() {return dishes;}

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
    public void setDishes(Dish dish) {this.dishes.add(dish);}
}

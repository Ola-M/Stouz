package com.example.stouz.models;

import java.util.List;

public class DishCategory {
    private String id;
    private String name;
    private String imageUrl;
    private List<Dish> dishes;


    public DishCategory(String id, String name, String imageUrl, List<Dish> dishes) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.dishes = dishes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }
}

package com.example.stouz.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class DishCategory implements Parcelable {
    private String id;
    private String name;
    private String imageUrl;
    private List<Dish> dishes;

    // Konstruktor
    public DishCategory(String id, String name, String imageUrl, List<Dish> dishes) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.dishes = dishes;
    }

    // Gettery i settery
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

    // Parcelable implementation
    protected DishCategory(Parcel in) {
        id = in.readString();
        name = in.readString();
        imageUrl = in.readString();
        dishes = in.createTypedArrayList(Dish.CREATOR);
    }

    public static final Creator<DishCategory> CREATOR = new Creator<DishCategory>() {
        @Override
        public DishCategory createFromParcel(Parcel in) {
            return new DishCategory(in);
        }

        @Override
        public DishCategory[] newArray(int size) {
            return new DishCategory[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(imageUrl);
        dest.writeTypedList(dishes);
    }
}

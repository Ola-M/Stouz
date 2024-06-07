package com.example.stouz.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class RestaurantMenu implements Parcelable {
    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private List<DishCategory> categories;

    public RestaurantMenu(String id, String name, String description, String imageUrl, List<DishCategory> categories) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.categories = categories;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<DishCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<DishCategory> categories) {
        this.categories = categories;
    }

    // Parcelable implementation
    protected RestaurantMenu(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        imageUrl = in.readString();
        categories = in.createTypedArrayList(DishCategory.CREATOR);
    }

    public static final Creator<RestaurantMenu> CREATOR = new Creator<RestaurantMenu>() {
        @Override
        public RestaurantMenu createFromParcel(Parcel in) {
            return new RestaurantMenu(in);
        }

        @Override
        public RestaurantMenu[] newArray(int size) {
            return new RestaurantMenu[size];
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
        dest.writeString(description);
        dest.writeString(imageUrl);
        dest.writeTypedList(categories);
    }
}

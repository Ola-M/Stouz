package com.example.stouz.models;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public class Restaurant implements Parcelable {
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

    // Constructor
    public Restaurant(String id, String name, String openingHours, double avgRating, String imageUrl, double latitude, double longitude, float distance, List<Comment> commentList, RestaurantMenu menu, List<String> userFavorites, int views) {
        this.id = id;
        this.name = name;
        this.openingHours = openingHours;
        this.avgRating = avgRating;
        this.imageUrl = imageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
        this.commentList = commentList;
        this.menu = menu;
        this.userFavorites = userFavorites;
        this.views = views;
    }

    // Getters and setters
    // ...

    // Parcelable implementation
    protected Restaurant(Parcel in) {
        id = in.readString();
        name = in.readString();
        openingHours = in.readString();
        avgRating = in.readDouble();
        imageUrl = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        distance = in.readFloat();
        commentList = in.createTypedArrayList(Comment.CREATOR);
        menu = in.readParcelable(RestaurantMenu.class.getClassLoader());
        userFavorites = in.createStringArrayList();
        views = in.readInt();
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
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
        dest.writeString(openingHours);
        dest.writeDouble(avgRating);
        dest.writeString(imageUrl);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeFloat(distance);
        dest.writeTypedList(commentList);
        dest.writeParcelable((Parcelable) menu, flags);
        dest.writeStringList(userFavorites);
        dest.writeInt(views);
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

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public double getAvgRating() {
        return calculateAvgRating();
    }

    public double calculateAvgRating() {
        if (commentList == null || commentList.isEmpty()) {
            return 0.0;
        }
        double total = 0.0;
        for (Comment comment : commentList) {
            total += comment.getRate();
        }
        return total / commentList.size();
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public RestaurantMenu getMenu() {
        return menu;
    }

    public void setMenu(RestaurantMenu menu) {
        this.menu = menu;
    }

    public List<String> getUserFavorites() {
        return userFavorites;
    }

    public void setUserFavorites(List<String> userFavorites) {
        this.userFavorites = userFavorites;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }
}

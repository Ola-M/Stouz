package com.example.stouz.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Comment implements Parcelable {
    private String id;
    private String user;
    private String description;
    private int rate;
    private String imageUrl;

    public Comment(String id, String user, String description, int rate, String imageUrl) {
        this.id = id;
        this.user = user;
        this.description = description;
        this.rate = rate;
        this.imageUrl = imageUrl;
    }

    protected Comment(Parcel in) {
        id = in.readString();
        user = in.readString();
        description = in.readString();
        rate = in.readInt();
        imageUrl = in.readString();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(user);
        dest.writeString(description);
        dest.writeInt(rate);
        dest.writeString(imageUrl);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

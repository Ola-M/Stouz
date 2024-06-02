package com.example.stouz.models;

public class Dish {
    private String id;
    private String name;
    private String description;
    private String sectionName;
    private String imageUrl;

    public Dish(){}
    public Dish(String id, String name, String description, String sectionName, String imageUrl){
        this.id = id;
        this.name = name;
        this.description = description;
        this.sectionName = sectionName;
        this.imageUrl = imageUrl;
    }

    public String getId() {return id;}
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getSectionName() {return sectionName;}
    public String getImageUrl() { return imageUrl;}

    public void setId(String id) {this.id = id;}
    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}


package com.ugur.safemealdeneme.Classes;

public class Categories {

    private String catId, name, image;
    
    public Categories(String catId, String name, String image) {
        this.catId = catId;
        this.name = name;
        this.image = image;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
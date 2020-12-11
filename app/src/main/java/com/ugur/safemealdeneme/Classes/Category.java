package com.ugur.safemealdeneme.Classes;

import android.net.Uri;

public class Category {
//Defining variables
    private String name;
    private Uri image;
    private String uuid;
//setting up constructor
    public Category(String name, Uri image, String uuid) {
        this.name = name;
        this.image = image;
        this.uuid = uuid;
    }
// Gtters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}

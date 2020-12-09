package com.ugur.safemealdeneme.Classes;

import android.net.Uri;

public class Category {

    private String name;
    private Uri image;
    private String uuid;

    public Category(String name, Uri image, String uuid) {
        this.name = name;
        this.image = image;
        this.uuid = uuid;
    }

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
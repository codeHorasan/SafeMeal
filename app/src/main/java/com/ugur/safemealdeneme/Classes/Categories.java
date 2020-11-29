package com.ugur.safemealdeneme.Classes;

import android.net.Uri;

public class Categories {

    private String name;
    private Uri image;

    public Categories(String name, Uri image) {
        this.name = name;
        this.image = image;
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
}
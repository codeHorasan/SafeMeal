package com.ugur.safemealdeneme.Models;

import android.net.Uri;

public class DepartmentProductModel {
    //Defining Variables
    private String productId;
    private String name, description;
    private double price;
    private Uri imageUri;

    public DepartmentProductModel() {
    }
    // Setting up constructors
    public DepartmentProductModel(String productId, String name, String description, double price, Uri imageUri) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUri = imageUri;
    }

    public DepartmentProductModel(String productId, String name, String description, double price) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
    }
    //Getters and Setters


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        System.out.println("Product ADII");
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }
}

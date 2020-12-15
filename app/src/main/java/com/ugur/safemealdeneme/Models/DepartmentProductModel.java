package com.ugur.safemealdeneme.Models;

import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DepartmentProductModel implements Comparable<DepartmentProductModel> {
    //Defining Variables
    private String productId;
    private String name, description;
    private double price;
    private Uri imageUri;
    private String dateString;

    public DepartmentProductModel() {
    }
    // Setting up constructors
    public DepartmentProductModel(String productId, String name, String description, double price, Uri imageUri, String dateString) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUri = imageUri;
        this.dateString = dateString;
    }

    public DepartmentProductModel(String productId, String name, String description, double price, String dateString) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.dateString = dateString;
    }
    //Getters and Setters


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    @Override
    public int compareTo(DepartmentProductModel o) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        try {
            Date thisDate = sdf.parse(this.dateString);
            Date otherDate = sdf.parse(o.dateString);
            if (thisDate.before(otherDate)) {
                return 1;
            } else {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Date Parsing Exception");
        }

        return 0;
    }
}

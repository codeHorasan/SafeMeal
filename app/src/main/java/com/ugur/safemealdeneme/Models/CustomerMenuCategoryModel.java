package com.ugur.safemealdeneme.Models;

import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomerMenuCategoryModel implements Comparable<CustomerMenuCategoryModel> {
    private Uri imageUri;
    private String categoryName;
    private String dateString;
    private String uuid;

    public CustomerMenuCategoryModel(Uri imageUri, String categoryName, String dateString, String uuid) {
        this.imageUri = imageUri;
        this.categoryName = categoryName;
        this.dateString = dateString;
        this.uuid = uuid;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getDateString() {
        return dateString;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public int compareTo(CustomerMenuCategoryModel o) {
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

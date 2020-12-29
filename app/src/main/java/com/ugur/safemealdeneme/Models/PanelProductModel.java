package com.ugur.safemealdeneme.Models;

import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PanelProductModel implements Comparable<PanelProductModel> {
    private String title;
    private String description;
    private float price;
    private String dateString;
    private String tableNo;
    private Uri imageUri;
    private String ID;
    private boolean done = false;

    public PanelProductModel(String title, String description, float price, String dateString, String tableNo) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.dateString = dateString;
        this.tableNo = tableNo;
    }

    public PanelProductModel(String title, String description, float price, String dateString, String tableNo, Uri imageUri) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.dateString = dateString;
        this.tableNo = tableNo;
        this.imageUri = imageUri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public int compareTo(PanelProductModel o) {
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

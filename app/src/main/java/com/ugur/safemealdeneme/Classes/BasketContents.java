package com.ugur.safemealdeneme.Classes;
import java.util.List;

public class BasketContents {

    private double price;
    private int servings;
    private String name;
    private List<String> imageList;

    public BasketContents(double price, int servings, String name, List<String> imageList) {
        this.price = price;
        this.servings = servings;
        this.name = name;
        this.imageList = imageList;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }
}

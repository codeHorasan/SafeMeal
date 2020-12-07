package com.ugur.safemealdeneme.Classes;

public class CompanyMenuCategoryItem {
    private int imageResource;
    private String categoryName;

    public CompanyMenuCategoryItem(int imageResource, String categoryName) {
        this.imageResource = imageResource;
        this.categoryName = categoryName;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getCategoryName() {
        return categoryName;
    }
}

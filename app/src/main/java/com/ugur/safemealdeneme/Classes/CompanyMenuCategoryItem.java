package com.ugur.safemealdeneme.Classes;

import android.net.Uri;

public class CompanyMenuCategoryItem implements Comparable<CompanyMenuCategoryItem> {
    private Uri imageUri;
    private String categoryName;
    private int sortingOrder;
    private String uuid;

    public CompanyMenuCategoryItem(Uri imageUri, String categoryName, int sortingOrder, String uuid) {
        this.imageUri = imageUri;
        this.categoryName = categoryName;
        this.sortingOrder = sortingOrder;
        this.uuid = uuid;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getSortingOrder() {
        return sortingOrder;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public int compareTo(CompanyMenuCategoryItem o) {
        if (this.sortingOrder < o.sortingOrder) {
            return 1;
        } else {
            return -1;
        }
    }
}

package com.ugur.safemealdeneme.Classes;

public class ProductBasket {

    private int productId, servings;

    public ProductBasket(int productId, int servings) {
        this.productId = productId;
        this.servings = servings;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }
}
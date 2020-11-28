package com.ugur.safemealdeneme.Classes;

import java.util.List;

public class Tables {

    private String name, status, OrderId;
    private List<String> imageList;

    public Tables(String name, String status, String OrderId,List<String> imageList ) {
        this.name = name;
        this.status = status;
        this.OrderId = OrderId;
        this.imageList = imageList;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        this.OrderId = orderId;
    }
    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }
}
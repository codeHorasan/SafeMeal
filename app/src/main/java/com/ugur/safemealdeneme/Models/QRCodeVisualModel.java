package com.ugur.safemealdeneme.Models;

public class QRCodeVisualModel {
    private String qrCompositeID;
    private String qrInfo;

    public QRCodeVisualModel(String qrCompositeID, String qrInfo) {
        this.qrCompositeID = qrCompositeID;
        this.qrInfo = qrInfo;
    }

    public String getQrCompositeID() {
        return qrCompositeID;
    }

    public String getQrInfo() {
        return qrInfo;
    }
}

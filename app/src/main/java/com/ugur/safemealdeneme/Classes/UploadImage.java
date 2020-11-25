package com.ugur.safemealdeneme.Classes;

public class UploadImage {
    private String companyUUID;
    private String imageUri;

    public UploadImage() {
    }

    public UploadImage(String companyUUID, String imageUri) {
        this.companyUUID = companyUUID;
        this.imageUri = imageUri;
    }

    public String getCompanyUUID() {
        return companyUUID;
    }

    public void setCompanyUUID(String companyUUID) {
        this.companyUUID = companyUUID;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}

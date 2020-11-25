package com.ugur.safemealdeneme.Classes;

import android.net.Uri;

import java.util.ArrayList;

public class Company {
    String email;
    String name;
    String UUID;
    ArrayList<Department> departmentList;
    Uri imageUri;

    private static Company company;

    private Company() {
        departmentList = new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public ArrayList<Department> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(ArrayList<Department> departmentList) {
        this.departmentList = departmentList;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public static Company getInstance() {
        if (company == null) {
            company = new Company();
        }
        return company;
    }
}

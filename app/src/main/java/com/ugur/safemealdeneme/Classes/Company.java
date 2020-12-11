package com.ugur.safemealdeneme.Classes;

import android.net.Uri;

import java.util.ArrayList;

public class Company {
    //Defining variables
    String email;
    String name;
    String UUID;
    ArrayList<Department> departmentList;
    ArrayList<Menu> menuList;
    Uri imageUri;

    private static Company company;
// Getting menu list and department list
    private Company() {
        departmentList = new ArrayList<>();
        menuList = new ArrayList<>();
    }
//Getters and Setters
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

    public ArrayList<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(ArrayList<Menu> menuList) {
        this.menuList = menuList;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }
// Gets Context(or isntance) of Company, if null make one
    public static Company getInstance() {
        if (company == null) {
            company = new Company();
        }
        return company;
    }
}

package com.ugur.safemealdeneme.Classes;

import com.ugur.safemealdeneme.Models.CustomerProductModel;

import java.util.ArrayList;

public class Customer {
    private String companyID;
    private String departmentID;
    private String menuID;
    private String tableNO;
    private ArrayList<CustomerProductModel> productModelList;

    private static Customer customer;

    private Customer() {
        productModelList = new ArrayList<>();
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(String departmentID) {
        this.departmentID = departmentID;
    }

    public String getMenuID() {
        return menuID;
    }

    public void setMenuID(String menuID) {
        this.menuID = menuID;
    }

    public String getTableNO() {
        return tableNO;
    }

    public void setTableNO(String tableNO) {
        this.tableNO = tableNO;
    }

    public ArrayList<CustomerProductModel> getProductModelList() {
        return productModelList;
    }

    public void setProductModelList(ArrayList<CustomerProductModel> productModelList) {
        this.productModelList = productModelList;
    }

    public static Customer getInstance() {
        if (customer == null) {
            customer = new Customer();
        }
        return customer;
    }
}

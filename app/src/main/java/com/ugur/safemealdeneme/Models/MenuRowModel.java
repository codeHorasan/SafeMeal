package com.ugur.safemealdeneme.Models;

import java.util.Date;

public class MenuRowModel implements Comparable<MenuRowModel> {
    private String name;
    private String uuid;
    private Date date;

    public MenuRowModel(String name, String uuid, Date date) {
        this.name = name;
        this.uuid = uuid;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
    }

    public Date getDate() {
        return date;
    }


    @Override
    public int compareTo(MenuRowModel o) {
        if (this.date.after(o.getDate())) {
            return 1;
        } else {
            return -1;
        }
    }
}

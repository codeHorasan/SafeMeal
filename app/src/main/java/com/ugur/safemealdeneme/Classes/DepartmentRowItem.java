package com.ugur.safemealdeneme.Classes;

import java.util.Date;

public class DepartmentRowItem implements Comparable<DepartmentRowItem> {
    private String name;
    private String uuid;
    private Date date;

    public DepartmentRowItem(String name, String uuid, Date date) {
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
    public int compareTo(DepartmentRowItem o) {
        if (this.date.after(o.getDate())) {
            return 1;
        } else {
            return -1;
        }
    }
}

package com.ugur.safemealdeneme.Classes;

import java.util.UUID;

public class Department {
    private String name;
    private String uuid;

    public Department(String name) {
        this.name = name;
        this.uuid = UUID.randomUUID().toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

}

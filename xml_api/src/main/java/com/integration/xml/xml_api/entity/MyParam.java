package com.integration.xml.xml_api.entity;

import java.io.Serializable;

public class MyParam implements Serializable{
    private String id;
    private String name;

    public MyParam() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

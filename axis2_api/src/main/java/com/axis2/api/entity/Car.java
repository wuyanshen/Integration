package com.axis2.api.entity;

import java.io.Serializable;

public class Car implements Serializable{
    private String name;
    private String price;
    private String decription;

    public Car() {
    }

    public Car(String name, String price ,String decription) {
        this.name = name;
        this.decription = decription;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    @Override
    public String toString() {
        return "Car{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", decription='" + decription + '\'' +
                '}';
    }
}

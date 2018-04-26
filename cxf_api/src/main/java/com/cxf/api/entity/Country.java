package com.cxf.api.entity;

import java.io.Serializable;

public class Country implements Serializable{
    private String name;
    private String population;
    private String capital;

    public Country() {
    }

    public Country(String name, String population, String capital) {
        this.name = name;
        this.population = population;
        this.capital = capital;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", population='" + population + '\'' +
                ", capital='" + capital + '\'' +
                '}';
    }
}

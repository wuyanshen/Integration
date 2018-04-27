package com.axis2.api.service;

import com.axis2.api.entity.Car;

import java.util.List;

public interface CarService {
    List<Car> findAllCars();
    Car findCarByName(String name);
}

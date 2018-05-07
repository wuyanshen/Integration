package com.axis2.api.service;

import com.axis2.api.entity.Car;

import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;

public interface CarService {
    List<Car> findAllCars();
    Car findCarByName(@WebParam(name = "carName") String carName);
}

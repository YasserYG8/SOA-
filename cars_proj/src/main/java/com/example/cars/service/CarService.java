package com.example.cars.service;

import java.util.List;
import com.example.cars.entities.Brand;
import com.example.cars.entities.Car;

public interface CarService {

    Car saveCar(Car car);
    Car updateCar(Car car);
    void deleteCar(Car car);
    void deleteCarById(Long id);
    Car getCar(Long id);
    List<Car> getAllCars();

    List<Car> findByModel(String model);
    List<Car> findByModelContains(String keyword);
    List<Car> findByModelPrice(String model, Double price);
    List<Car> findByBrand(Brand brand);
    List<Car> findByBrandIdBrand(Long id);
    List<Car> findByOrderByModelAsc();
    List<Car> sortCarsByModelPrice();

    List<Brand> getAllBrands();
    Brand getBrand(Long id);
    Brand saveBrand(Brand brand);
}

package com.example.cars.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.cars.entities.Brand;
import com.example.cars.entities.Car;
import com.example.cars.repos.BrandRepository;
import com.example.cars.repos.CarRepository;

@Service
public class CarServiceImpl implements CarService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Override
    public Car saveCar(Car car) {
        return carRepository.save(car);
    }

    @Override
    public Car updateCar(Car car) {
        return carRepository.save(car);
    }

    @Override
    public void deleteCar(Car car) {
        carRepository.delete(car);
    }

    @Override
    public void deleteCarById(Long id) {
        carRepository.deleteById(id);
    }

    @Override
    public Car getCar(Long id) {
        return carRepository.findById(id).orElse(null);
    }

    @Override
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    @Override
    public List<Car> findByModel(String model) {
        return carRepository.findByModel(model);
    }

    @Override
    public List<Car> findByModelContains(String keyword) {
        return carRepository.findByModelContains(keyword);
    }

    @Override
    public List<Car> findByModelPrice(String model, Double price) {
        return carRepository.findByModelPrice(model, price);
    }

    @Override
    public List<Car> findByBrand(Brand brand) {
        return carRepository.findByBrand(brand);
    }

    @Override
    public List<Car> findByBrandIdBrand(Long id) {
        return carRepository.findByBrandIdBrand(id);
    }

    @Override
    public List<Car> findByOrderByModelAsc() {
        return carRepository.findByOrderByModelAsc();
    }

    @Override
    public List<Car> sortCarsByModelPrice() {
        return carRepository.sortCarsByModelPrice();
    }

    @Override
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    @Override
    public Brand getBrand(Long id) {
        return brandRepository.findById(id).orElse(null);
    }

    @Override
    public Brand saveBrand(Brand brand) {
        return brandRepository.save(brand);
    }
}

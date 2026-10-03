package com.example.cars.restcontrollers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.example.cars.entities.Brand;
import com.example.cars.entities.Car;
import com.example.cars.service.CarService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CarRESTController {

    @Autowired
    private CarService carService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping("/all")
    public List<Car> getAllCarsList() {
        return carService.getAllCars();
    }

    @GetMapping("/{id}")
    public Car getCarById(@PathVariable("id") Long id) {
        return carService.getCar(id);
    }

    @PostMapping
    public Car createCar(@RequestBody Car car) {
        return carService.saveCar(car);
    }

    @PutMapping
    public Car updateCar(@RequestBody Car car) {
        return carService.updateCar(car);
    }

    @DeleteMapping("/{id}")
    public void deleteCar(@PathVariable("id") Long id) {
        carService.deleteCarById(id);
    }

    @GetMapping("/carsbrand/{idBrand}")
    public List<Car> getCarsByBrandId(@PathVariable("idBrand") Long idBrand) {
        return carService.findByBrandIdBrand(idBrand);
    }

    @GetMapping("/carsbymodel/{model}")
    public List<Car> getCarsByModel(@PathVariable("model") String model) {
        return carService.findByModelContains(model);
    }

    @GetMapping("/brands")
    public List<Brand> getAllBrands() {
        return carService.getAllBrands();
    }

    @GetMapping("/brands/{id}")
    public Brand getBrandById(@PathVariable("id") Long id) {
        return carService.getBrand(id);
    }
}

package com.example.cars;

import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.cars.entities.Brand;
import com.example.cars.entities.Car;
import com.example.cars.repos.BrandRepository;
import com.example.cars.repos.CarRepository;

@SpringBootTest
class CarsProjApplicationTests {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Test
    public void testCreateCar() {
        Brand brand = new Brand("Ferrari", "Italy", "Scuderia Ferrari luxury sports cars");
        brandRepository.save(brand);

        Car car = new Car("296 GTB", 270000.0, new Date(), "FERRARI-296", brand);
        carRepository.save(car);
        System.out.println("Created car: " + car);
    }

    @Test
    public void testFindCar() {
        List<Car> cars = carRepository.findAll();
        if (!cars.isEmpty()) {
            Car c = carRepository.findById(cars.get(0).getIdCar()).orElse(null);
            System.out.println("Found car: " + c);
        }
    }

    @Test
    public void testUpdateCar() {
        List<Car> cars = carRepository.findAll();
        if (!cars.isEmpty()) {
            Car c = cars.get(0);
            c.setPrice(c.getPrice() + 1000.0);
            carRepository.save(c);
            System.out.println("Updated car price: " + c.getPrice());
        }
    }

    @Test
    public void testListerTousCars() {
        List<Car> cars = carRepository.findAll();
        for (Car c : cars) {
            System.out.println(c);
        }
    }

    @Test
    public void testFindByModel() {
        List<Car> cars = carRepository.findByModel("M4 Competition");
        for (Car c : cars) {
            System.out.println("findByModel: " + c);
        }
    }

    @Test
    public void testFindByModelContains() {
        List<Car> cars = carRepository.findByModelContains("GT");
        for (Car c : cars) {
            System.out.println("findByModelContains 'GT': " + c);
        }
    }

    @Test
    public void testFindByModelPrice() {
        List<Car> cars = carRepository.findByModelPrice("M", 50000.0);
        for (Car c : cars) {
            System.out.println("findByModelPrice: " + c);
        }
    }

    @Test
    public void testFindByBrand() {
        List<Brand> brands = brandRepository.findAll();
        if (!brands.isEmpty()) {
            List<Car> cars = carRepository.findByBrand(brands.get(0));
            for (Car c : cars) {
                System.out.println("findByBrand: " + c);
            }
        }
    }

    @Test
    public void testFindByBrandIdBrand() {
        List<Brand> brands = brandRepository.findAll();
        if (!brands.isEmpty()) {
            List<Car> cars = carRepository.findByBrandIdBrand(brands.get(0).getIdBrand());
            for (Car c : cars) {
                System.out.println("findByBrandIdBrand: " + c);
            }
        }
    }

    @Test
    public void testSortCarsByModelPrice() {
        List<Car> cars = carRepository.sortCarsByModelPrice();
        for (Car c : cars) {
            System.out.println("Sorted: " + c);
        }
    }
}

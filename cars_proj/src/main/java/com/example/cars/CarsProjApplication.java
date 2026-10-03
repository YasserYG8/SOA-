package com.example.cars;

import java.text.SimpleDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.example.cars.entities.Brand;
import com.example.cars.entities.Car;
import com.example.cars.service.CarService;

@SpringBootApplication
public class CarsProjApplication implements CommandLineRunner {

    @Autowired
    private CarService carService;

    public static void main(String[] args) {
        SpringApplication.run(CarsProjApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        if (carService.getAllBrands().isEmpty()) {
            Brand bmw = carService.saveBrand(new Brand("BMW", "Germany", "Bayerische Motoren Werke - High performance & luxury"));
            Brand audi = carService.saveBrand(new Brand("Audi", "Germany", "Vorsprung durch Technik - Premium sporty sedans and SUVs"));
            Brand merc = carService.saveBrand(new Brand("Mercedes-Benz", "Germany", "The best or nothing - Luxury and AMG engineering"));
            Brand porsche = carService.saveBrand(new Brand("Porsche", "Germany", "Exceptional sports cars and precision racing engineering"));

            carService.saveCar(new Car("M4 Competition", 88500.0, sdf.parse("2023-04-12"), "235-TUN-4040", bmw));
            carService.saveCar(new Car("RS6 Avant", 126000.0, sdf.parse("2022-10-18"), "238-TUN-6060", audi));
            carService.saveCar(new Car("AMG GT Coupé", 145000.0, sdf.parse("2023-07-25"), "241-TUN-7070", merc));
            carService.saveCar(new Car("911 GT3 RS", 189000.0, sdf.parse("2024-02-14"), "245-TUN-9110", porsche));
            carService.saveCar(new Car("M3 Touring", 92000.0, sdf.parse("2023-09-01"), "242-TUN-3030", bmw));
            carService.saveCar(new Car("RS3 Sportback", 68000.0, sdf.parse("2023-01-20"), "236-TUN-3333", audi));
        }
    }
}

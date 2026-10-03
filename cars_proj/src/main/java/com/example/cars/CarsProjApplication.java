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

            carService.saveCar(new Car("M4 Competition", 88500.0, sdf.parse("2023-04-12"), "235-TUN-4040", 
                "https://images.unsplash.com/photo-1617814076367-b759c7d7e738?auto=format&fit=crop&w=800&q=80", bmw));
            carService.saveCar(new Car("RS6 Avant", 126000.0, sdf.parse("2022-10-18"), "238-TUN-6060", 
                "https://images.unsplash.com/photo-1603584173870-7f23fdae1b7a?auto=format&fit=crop&w=800&q=80", audi));
            carService.saveCar(new Car("AMG GT Coupé", 145000.0, sdf.parse("2023-07-25"), "241-TUN-7070", 
                "https://images.unsplash.com/photo-1618843479313-40f8afb4b4d8?auto=format&fit=crop&w=800&q=80", merc));
            carService.saveCar(new Car("911 GT3 RS", 189000.0, sdf.parse("2024-02-14"), "245-TUN-9110", 
                "https://images.unsplash.com/photo-1614162692292-7ac56d7f7f1e?auto=format&fit=crop&w=800&q=80", porsche));
            carService.saveCar(new Car("M3 Touring", 92000.0, sdf.parse("2023-09-01"), "242-TUN-3030", 
                "https://images.unsplash.com/photo-1580273916550-e323be2ae537?auto=format&fit=crop&w=800&q=80", bmw));
            carService.saveCar(new Car("RS3 Sportback", 68000.0, sdf.parse("2023-01-20"), "236-TUN-3333", 
                "https://images.unsplash.com/photo-1541348263662-e0c86667a92f?auto=format&fit=crop&w=800&q=80", audi));
        }
    }
}

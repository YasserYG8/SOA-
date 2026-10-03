package com.example.cars.repos;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.cars.entities.Brand;
import com.example.cars.entities.Car;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    // Query methods requested in Section 2 of the course
    List<Car> findByModel(String model);

    List<Car> findByModelContains(String keyword);

    @Query("select c from Car c where c.model like %:model% and c.price > :price")
    List<Car> findByModelPrice(@Param("model") String model, @Param("price") Double price);

    @Query("select c from Car c where c.brand = :brand")
    List<Car> findByBrand(@Param("brand") Brand brand);

    List<Car> findByBrandIdBrand(Long idBrand);

    List<Car> findByOrderByModelAsc();

    @Query("select c from Car c order by c.model ASC, c.price DESC")
    List<Car> sortCarsByModelPrice();
}

package com.example.cars.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import com.example.cars.entities.Brand;

@Repository
@RepositoryRestResource(path = "brand")
public interface BrandRepository extends JpaRepository<Brand, Long> {
    Brand findByName(String name);
}

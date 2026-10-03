package com.example.cars.entities;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCar;

    private String model;
    private Double price;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date releaseDate;

    private String licensePlate;
    private String imagePath;

    @ManyToOne
    private Brand brand;

    public Car() {
        super();
    }

    public Car(String model, Double price, Date releaseDate, String licensePlate, String imagePath, Brand brand) {
        super();
        this.model = model;
        this.price = price;
        this.releaseDate = releaseDate;
        this.licensePlate = licensePlate;
        this.imagePath = imagePath;
        this.brand = brand;
    }

    public Car(String model, Double price, Date releaseDate, String licensePlate, Brand brand) {
        super();
        this.model = model;
        this.price = price;
        this.releaseDate = releaseDate;
        this.licensePlate = licensePlate;
        this.brand = brand;
    }

    public Car(Long idCar, String model, Double price, Date releaseDate, String licensePlate, String imagePath, Brand brand) {
        super();
        this.idCar = idCar;
        this.model = model;
        this.price = price;
        this.releaseDate = releaseDate;
        this.licensePlate = licensePlate;
        this.imagePath = imagePath;
        this.brand = brand;
    }

    public Long getIdCar() {
        return idCar;
    }

    public void setIdCar(Long idCar) {
        this.idCar = idCar;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    @Override
    public String toString() {
        return "Car [idCar=" + idCar + ", model=" + model + ", price=" + price + ", releaseDate=" + releaseDate
                + ", licensePlate=" + licensePlate + ", imagePath=" + imagePath + ", brand=" + (brand != null ? brand.getName() : null) + "]";
    }
}

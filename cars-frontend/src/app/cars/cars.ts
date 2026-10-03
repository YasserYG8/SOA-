import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Car } from '../model/car.model';
import { Brand } from '../model/brand.model';
import { CarService } from '../services/car.service';

@Component({
  selector: 'app-cars',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './cars.html',
  styleUrl: './cars.css',
})
export class CarsComponent implements OnInit {

  cars: Car[] = [];
  brands: Brand[] = [];
  selectedBrandId: number = 0;
  searchKeyword: string = '';

  constructor(private carService: CarService) {}

  ngOnInit(): void {
    this.loadCars();
    this.loadBrands();
  }

  loadCars(): void {
    this.carService.carList().subscribe({
      next: (data) => {
        this.cars = data;
      },
      error: (err) => console.error('Error loading cars', err)
    });
  }

  loadBrands(): void {
    this.carService.brandList().subscribe({
      next: (data) => {
        this.brands = data;
      },
      error: (err) => console.error('Error loading brands', err)
    });
  }

  deleteCar(c: Car): void {
    const conf = confirm(`Are you sure you want to delete the car "${c.model}"?`);
    if (conf) {
      this.carService.deleteCar(c.idCar).subscribe({
        next: () => {
          this.loadCars();
        },
        error: (err) => console.error('Error deleting car', err)
      });
    }
  }

  onBrandFilterChange(): void {
    if (this.selectedBrandId == 0) {
      this.loadCars();
    } else {
      this.carService.filterCarsByBrand(this.selectedBrandId).subscribe({
        next: (data) => {
          this.cars = data;
        },
        error: (err) => console.error('Error filtering cars by brand', err)
      });
    }
  }

  onSearch(): void {
    if (!this.searchKeyword || this.searchKeyword.trim() === '') {
      this.loadCars();
    } else {
      this.carService.searchCarsByModel(this.searchKeyword.trim()).subscribe({
        next: (data) => {
          this.cars = data;
        },
        error: (err) => console.error('Error searching cars', err)
      });
    }
  }
}

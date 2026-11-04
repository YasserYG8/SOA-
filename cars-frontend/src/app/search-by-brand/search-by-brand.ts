import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Car } from '../model/car.model';
import { Brand } from '../model/brand.model';
import { CarService } from '../services/car.service';

@Component({
  selector: 'app-search-by-brand',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './search-by-brand.html',
})
export class SearchByBrandComponent implements OnInit {

  cars: Car[] = [];
  brands: Brand[] = [];
  idBrand: number = 0;
  errorMessage: string = '';

  constructor(private carService: CarService) {}

  ngOnInit(): void {
    this.carService.brandList().subscribe({
      next: (b) => this.brands = b._embedded.brands,
      error: () => this.errorMessage = 'Could not load the brands.'
    });
  }

  onChange(): void {
    this.cars = [];
    if (!this.idBrand) {
      return;
    }
    this.carService.filterCarsByBrand(this.idBrand).subscribe({
      next: (cars) => this.cars = cars,
      error: () => this.errorMessage = 'Search failed. Please retry.'
    });
  }
}

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { Car } from '../model/car.model';
import { Brand } from '../model/brand.model';
import { CarService } from '../services/car.service';

@Component({
  selector: 'app-add-car',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './add-car.html',
  styleUrl: './add-car.css',
})
export class AddCarComponent implements OnInit {

  newCar = new Car();
  brands: Brand[] = [];
  newIdBrand!: number;
  message: string = '';

  constructor(private carService: CarService, private router: Router) {}

  ngOnInit(): void {
    this.carService.brandList().subscribe({
      next: (b) => {
        this.brands = b;
        if (this.brands.length > 0) {
          this.newIdBrand = this.brands[0].idBrand;
        }
      },
      error: (err) => console.error('Error fetching brands', err)
    });
  }

  addCar(): void {
    this.newCar.brand = this.brands.find(b => b.idBrand == this.newIdBrand)!;
    this.carService.addCar(this.newCar).subscribe({
      next: (car) => {
        this.message = `Car "${car.model}" added successfully!`;
        setTimeout(() => {
          this.router.navigate(['/cars']);
        }, 1200);
      },
      error: (err) => console.error('Error adding car', err)
    });
  }
}

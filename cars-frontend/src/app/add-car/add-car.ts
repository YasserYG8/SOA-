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
  fallbackImage: string = 'https://images.unsplash.com/photo-1503376780353-7e6692767b70?auto=format&fit=crop&w=800&q=80';

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
    if (!this.newCar.imagePath || this.newCar.imagePath.trim() === '') {
      this.newCar.imagePath = this.fallbackImage;
    }

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

  onImageError(event: any): void {
    event.target.src = this.fallbackImage;
  }
}

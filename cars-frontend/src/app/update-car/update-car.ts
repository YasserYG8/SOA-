import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Car } from '../model/car.model';
import { Brand } from '../model/brand.model';
import { CarService } from '../services/car.service';

@Component({
  selector: 'app-update-car',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './update-car.html',
  styleUrl: './update-car.css',
})
export class UpdateCarComponent implements OnInit {

  currentCar = new Car();
  brands: Brand[] = [];
  updatedBrandId!: number;
  message: string = '';

  constructor(
    private carService: CarService,
    private activatedRoute: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = this.activatedRoute.snapshot.params['id'];

    this.carService.brandList().subscribe({
      next: (b) => {
        this.brands = b;
      },
      error: (err) => console.error('Error fetching brands', err)
    });

    this.carService.consultCar(id).subscribe({
      next: (c) => {
        this.currentCar = c;
        if (this.currentCar.brand) {
          this.updatedBrandId = this.currentCar.brand.idBrand;
        }
      },
      error: (err) => console.error('Error fetching car details', err)
    });
  }

  updateCar(): void {
    if (this.updatedBrandId) {
      this.currentCar.brand = this.brands.find(b => b.idBrand == this.updatedBrandId)!;
    }
    this.carService.updateCar(this.currentCar).subscribe({
      next: (car) => {
        this.message = `Car #${car.idCar} updated successfully!`;
        setTimeout(() => {
          this.router.navigate(['/cars']);
        }, 1200);
      },
      error: (err) => console.error('Error updating car', err)
    });
  }
}

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { Car } from '../model/car.model';
import { Brand } from '../model/brand.model';
import { CarService } from '../services/car.service';

interface CarPreset {
  model: string;
  brandName: string;
  price: number;
  licensePlate: string;
  releaseDate: string;
  imagePath: string;
}

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

  presets: CarPreset[] = [
    {
      model: 'M8 Gran Coupé',
      brandName: 'BMW',
      price: 155000,
      licensePlate: '252-TUN-8800',
      releaseDate: '2024-03-15',
      imagePath: 'https://images.unsplash.com/photo-1555215695-3004980ad54e?auto=format&fit=crop&w=800&q=80'
    },
    {
      model: 'RS e-tron GT',
      brandName: 'Audi',
      price: 145000,
      licensePlate: '254-TUN-1010',
      releaseDate: '2024-01-20',
      imagePath: 'https://images.unsplash.com/photo-1617788138017-80ad40651399?auto=format&fit=crop&w=800&q=80'
    },
    {
      model: 'SL 63 AMG',
      brandName: 'Mercedes-Benz',
      price: 178000,
      licensePlate: '255-TUN-6363',
      releaseDate: '2023-11-05',
      imagePath: 'https://images.unsplash.com/photo-1618843479313-40f8afb4b4d8?auto=format&fit=crop&w=800&q=80'
    },
    {
      model: '911 Turbo S',
      brandName: 'Porsche',
      price: 220000,
      licensePlate: '258-TUN-9999',
      releaseDate: '2024-06-01',
      imagePath: 'https://images.unsplash.com/photo-1503376780353-7e6692767b70?auto=format&fit=crop&w=800&q=80'
    }
  ];

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

  applyPreset(preset: CarPreset): void {
    this.newCar.model = preset.model;
    this.newCar.price = preset.price;
    this.newCar.licensePlate = preset.licensePlate;
    this.newCar.imagePath = preset.imagePath;
    this.newCar.releaseDate = new Date(preset.releaseDate);

    const match = this.brands.find(b => b.name.toLowerCase() === preset.brandName.toLowerCase());
    if (match) {
      this.newIdBrand = match.idBrand;
    }
  }

  addCar(): void {
    this.newCar.brand = this.brands.find(b => b.idBrand == this.newIdBrand)!;
    if (!this.newCar.imagePath || this.newCar.imagePath.trim() === '') {
      this.newCar.imagePath = this.fallbackImage;
    }

    this.carService.addCar(this.newCar).subscribe({
      next: (car) => {
        this.message = `Vehicle "${car.model}" successfully registered!`;
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

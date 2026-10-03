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
  filteredCars: Car[] = [];
  brands: Brand[] = [];
  selectedBrandId: number = 0;
  searchKeyword: string = '';
  sortBy: string = 'name-asc';
  viewMode: 'table' | 'grid' = 'grid';
  isLoading: boolean = true;

  // Modals & Feedback
  selectedCarForModal: Car | null = null;
  carToDelete: Car | null = null;
  toastMessage: string = '';
  toastType: 'success' | 'error' = 'success';

  fallbackImage: string = 'https://images.unsplash.com/photo-1503376780353-7e6692767b70?auto=format&fit=crop&w=800&q=80';

  constructor(private carService: CarService) {}

  ngOnInit(): void {
    this.loadInitialData();
  }

  loadInitialData(): void {
    this.isLoading = true;
    this.carService.brandList().subscribe({
      next: (b) => {
        this.brands = b;
      },
      error: (err) => console.error('Error loading brands', err)
    });

    this.carService.carList().subscribe({
      next: (data) => {
        this.cars = data;
        this.applyFiltersAndSorting();
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error loading cars', err);
        this.isLoading = false;
        this.showToast('Failed to connect to backend server', 'error');
      }
    });
  }

  loadCars(): void {
    this.carService.carList().subscribe({
      next: (data) => {
        this.cars = data;
        this.applyFiltersAndSorting();
      },
      error: (err) => console.error('Error loading cars', err)
    });
  }

  applyFiltersAndSorting(): void {
    let result = [...this.cars];

    // Filter by Brand
    if (this.selectedBrandId && this.selectedBrandId != 0) {
      result = result.filter(c => c.brand && c.brand.idBrand == this.selectedBrandId);
    }

    // Filter by search keyword
    if (this.searchKeyword && this.searchKeyword.trim() !== '') {
      const q = this.searchKeyword.toLowerCase().trim();
      result = result.filter(c => 
        (c.model && c.model.toLowerCase().includes(q)) ||
        (c.licensePlate && c.licensePlate.toLowerCase().includes(q)) ||
        (c.brand && c.brand.name.toLowerCase().includes(q))
      );
    }

    // Sorting
    switch (this.sortBy) {
      case 'price-asc':
        result.sort((a, b) => (a.price || 0) - (b.price || 0));
        break;
      case 'price-desc':
        result.sort((a, b) => (b.price || 0) - (a.price || 0));
        break;
      case 'date-desc':
        result.sort((a, b) => new Date(b.releaseDate).getTime() - new Date(a.releaseDate).getTime());
        break;
      case 'name-asc':
      default:
        result.sort((a, b) => (a.model || '').localeCompare(b.model || ''));
        break;
    }

    this.filteredCars = result;
  }

  onBrandFilterChange(): void {
    this.applyFiltersAndSorting();
  }

  onSearch(): void {
    this.applyFiltersAndSorting();
  }

  onSortChange(): void {
    this.applyFiltersAndSorting();
  }

  clearSearch(): void {
    this.searchKeyword = '';
    this.selectedBrandId = 0;
    this.applyFiltersAndSorting();
  }

  // Delete modal flows
  confirmDelete(c: Car): void {
    this.carToDelete = c;
  }

  cancelDelete(): void {
    this.carToDelete = null;
  }

  executeDelete(): void {
    if (!this.carToDelete) return;
    const modelName = this.carToDelete.model;
    const id = this.carToDelete.idCar;

    this.carService.deleteCar(id).subscribe({
      next: () => {
        this.cars = this.cars.filter(c => c.idCar !== id);
        this.applyFiltersAndSorting();
        this.carToDelete = null;
        this.showToast(`Vehicle "${modelName}" successfully removed.`, 'success');
      },
      error: (err) => {
        console.error('Error deleting car', err);
        this.carToDelete = null;
        this.showToast('Could not delete vehicle. Please retry.', 'error');
      }
    });
  }

  // Quick View Modal
  openQuickView(c: Car): void {
    this.selectedCarForModal = c;
  }

  closeQuickView(): void {
    this.selectedCarForModal = null;
  }

  showToast(msg: string, type: 'success' | 'error' = 'success'): void {
    this.toastMessage = msg;
    this.toastType = type;
    setTimeout(() => {
      this.toastMessage = '';
    }, 3500);
  }

  // Stats calculation
  get averagePrice(): number {
    if (!this.cars.length) return 0;
    const total = this.cars.reduce((sum, c) => sum + (c.price || 0), 0);
    return Math.round(total / this.cars.length);
  }

  onImageError(event: any): void {
    event.target.src = this.fallbackImage;
  }
}

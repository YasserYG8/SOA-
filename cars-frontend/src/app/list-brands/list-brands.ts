import { Component, OnInit } from '@angular/core';
import { Brand } from '../model/brand.model';
import { CarService } from '../services/car.service';
import { UpdateBrandComponent } from '../update-brand/update-brand';

@Component({
  selector: 'app-list-brands',
  standalone: true,
  imports: [UpdateBrandComponent],
  templateUrl: './list-brands.html',
})
export class ListBrandsComponent implements OnInit {

  brands: Brand[] = [];
  updatedBrand: Brand = new Brand();
  add: boolean = true;
  errorMessage: string = '';

  constructor(private carService: CarService) {}

  ngOnInit(): void {
    this.loadBrands();
  }

  loadBrands(): void {
    this.carService.brandList().subscribe({
      next: (b) => this.brands = b._embedded.brands,
      error: () => this.errorMessage = 'Could not load the brands.'
    });
  }

  // Send a copy down, so typing in the form does not change the list until it is saved.
  editBrand(brand: Brand): void {
    this.updatedBrand = { ...brand };
    this.add = false;
  }

  newBrand(): void {
    this.updatedBrand = new Brand();
    this.add = true;
  }

  // Receives the child's @Output event, then saves and refreshes the list.
  brandUpdated(brand: Brand): void {
    const request = this.add ? this.carService.addBrand(brand) : this.carService.updateBrand(brand);
    request.subscribe({
      next: () => {
        this.errorMessage = '';
        this.newBrand();
        this.loadBrands();
      },
      error: () => this.errorMessage = 'Could not save the brand. Please retry.'
    });
  }
}

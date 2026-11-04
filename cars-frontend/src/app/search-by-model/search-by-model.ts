import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Car } from '../model/car.model';
import { CarService } from '../services/car.service';
import { SearchFilterPipe } from '../pipes/search-filter.pipe';

type SearchMode = 'api' | 'keyup' | 'pipe';

@Component({
  selector: 'app-search-by-model',
  standalone: true,
  imports: [CommonModule, FormsModule, SearchFilterPipe],
  templateUrl: './search-by-model.html',
})
export class SearchByModelComponent implements OnInit {

  mode: SearchMode = 'api';
  modes: { id: SearchMode; label: string }[] = [
    { id: 'api', label: 'Server (button)' },
    { id: 'keyup', label: 'Keyup event' },
    { id: 'pipe', label: 'Custom pipe' }
  ];

  allCars: Car[] = [];
  cars: Car[] = [];
  keyword: string = '';
  searchTerm: string = '';
  errorMessage: string = '';

  constructor(private carService: CarService) {}

  ngOnInit(): void {
    this.carService.carList().subscribe({
      next: (cars) => {
        this.allCars = cars;
        this.cars = cars;
      },
      error: () => this.errorMessage = 'Could not load the cars.'
    });
  }

  setMode(mode: SearchMode): void {
    this.mode = mode;
    this.keyword = '';
    this.searchTerm = '';
    this.cars = this.allCars;
  }

  // Technique 1: the server runs the search.
  searchByModel(): void {
    const keyword = this.keyword.trim();
    if (!keyword) {
      this.cars = this.allCars;
      return;
    }
    this.carService.searchCarsByModel(keyword).subscribe({
      next: (cars) => this.cars = cars,
      error: () => this.errorMessage = 'Search failed. Please retry.'
    });
  }

  // Technique 2: filter in the class on every keystroke.
  onKeyUp(e: Event): void {
    const val = (e.target as HTMLInputElement).value.toLowerCase();
    this.cars = this.allCars.filter(c => c.model.toLowerCase().includes(val));
  }
}

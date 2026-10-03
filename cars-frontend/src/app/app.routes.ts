import { Routes } from '@angular/router';
import { CarsComponent } from './cars/cars';
import { AddCarComponent } from './add-car/add-car';
import { UpdateCarComponent } from './update-car/update-car';

export const routes: Routes = [
  { path: 'cars', component: CarsComponent },
  { path: 'add-car', component: AddCarComponent },
  { path: 'update-car/:id', component: UpdateCarComponent },
  { path: '', redirectTo: '/cars', pathMatch: 'full' }
];

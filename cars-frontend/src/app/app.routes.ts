import { Routes } from '@angular/router';
import { CarsComponent } from './cars/cars';
import { AddCarComponent } from './add-car/add-car';
import { UpdateCarComponent } from './update-car/update-car';
import { SearchByBrandComponent } from './search-by-brand/search-by-brand';
import { SearchByModelComponent } from './search-by-model/search-by-model';
import { ListBrandsComponent } from './list-brands/list-brands';
import { LoginComponent } from './login/login';
import { RegisterComponent } from './register/register';
import { VerifyEmailComponent } from './verify-email/verify-email';
import { ForbiddenComponent } from './forbidden/forbidden';
import { authGuard } from './guards/auth.guard';
import { carGuard } from './guards/car.guard';

export const routes: Routes = [
  { path: 'cars', component: CarsComponent, canActivate: [authGuard] },
  { path: 'add-car', component: AddCarComponent, canActivate: [authGuard, carGuard] },
  { path: 'update-car/:id', component: UpdateCarComponent, canActivate: [authGuard, carGuard] },
  { path: 'search-by-brand', component: SearchByBrandComponent, canActivate: [authGuard] },
  { path: 'search-by-model', component: SearchByModelComponent, canActivate: [authGuard] },
  { path: 'brands', component: ListBrandsComponent, canActivate: [authGuard, carGuard] },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'verifyEmail', component: VerifyEmailComponent },
  { path: 'app-forbidden', component: ForbiddenComponent },
  { path: '', redirectTo: '/cars', pathMatch: 'full' }
];

import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Car } from '../model/car.model';
import { Brand } from '../model/brand.model';
import { BrandWrapper } from '../model/brand-wrapper.model';
import { environment } from '../../environments/environment';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class CarService {

  apiURL: string = environment.apiURL;

  constructor(private http: HttpClient, private authService: AuthService) { }

  private headers(): HttpHeaders {
    return new HttpHeaders({ 'Authorization': 'Bearer ' + this.authService.getToken() });
  }

  carList(): Observable<Car[]> {
    return this.http.get<Car[]>(this.apiURL + '/all', { headers: this.headers() });
  }

  addCar(car: Car): Observable<Car> {
    return this.http.post<Car>(this.apiURL, car, { headers: this.headers() });
  }

  deleteCar(id: number): Observable<any> {
    const url = `${this.apiURL}/${id}`;
    return this.http.delete(url, { headers: this.headers() });
  }

  consultCar(id: number): Observable<Car> {
    const url = `${this.apiURL}/${id}`;
    return this.http.get<Car>(url, { headers: this.headers() });
  }

  updateCar(car: Car): Observable<Car> {
    return this.http.put<Car>(this.apiURL, car, { headers: this.headers() });
  }

  brandList(): Observable<BrandWrapper> {
    return this.http.get<BrandWrapper>(environment.apiURLBrand, { headers: this.headers() });
  }

  addBrand(brand: Brand): Observable<Brand> {
    return this.http.post<Brand>(environment.apiURLBrand, brand, { headers: this.headers() });
  }

  updateBrand(brand: Brand): Observable<Brand> {
    return this.http.put<Brand>(`${environment.apiURLBrand}/${brand.idBrand}`, brand, { headers: this.headers() });
  }

  filterCarsByBrand(idBrand: number): Observable<Car[]> {
    const url = `${this.apiURL}/carsbrand/${idBrand}`;
    return this.http.get<Car[]>(url, { headers: this.headers() });
  }

  searchCarsByModel(model: string): Observable<Car[]> {
    const url = `${this.apiURL}/carsbymodel/${encodeURIComponent(model)}`;
    return this.http.get<Car[]>(url, { headers: this.headers() });
  }
}

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Car } from '../model/car.model';
import { Brand } from '../model/brand.model';
import { BrandWrapper } from '../model/brand-wrapper.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CarService {

  apiURL: string = environment.apiURL;

  constructor(private http: HttpClient) { }

  carList(): Observable<Car[]> {
    return this.http.get<Car[]>(this.apiURL + '/all');
  }

  addCar(car: Car): Observable<Car> {
    return this.http.post<Car>(this.apiURL, car);
  }

  deleteCar(id: number): Observable<any> {
    const url = `${this.apiURL}/${id}`;
    return this.http.delete(url);
  }

  consultCar(id: number): Observable<Car> {
    const url = `${this.apiURL}/${id}`;
    return this.http.get<Car>(url);
  }

  updateCar(car: Car): Observable<Car> {
    return this.http.put<Car>(this.apiURL, car);
  }

  brandList(): Observable<BrandWrapper> {
    return this.http.get<BrandWrapper>(environment.apiURLBrand);
  }

  addBrand(brand: Brand): Observable<Brand> {
    return this.http.post<Brand>(environment.apiURLBrand, brand);
  }

  updateBrand(brand: Brand): Observable<Brand> {
    return this.http.put<Brand>(`${environment.apiURLBrand}/${brand.idBrand}`, brand);
  }

  filterCarsByBrand(idBrand: number): Observable<Car[]> {
    const url = `${this.apiURL}/carsbrand/${idBrand}`;
    return this.http.get<Car[]>(url);
  }

  searchCarsByModel(model: string): Observable<Car[]> {
    const url = `${this.apiURL}/carsbymodel/${encodeURIComponent(model)}`;
    return this.http.get<Car[]>(url);
  }
}

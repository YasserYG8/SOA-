import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Car } from '../model/car.model';
import { Brand } from '../model/brand.model';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class CarService {

  apiURL: string = 'http://localhost:8082/api';

  constructor(private http: HttpClient) { }

  carList(): Observable<Car[]> {
    return this.http.get<Car[]>(this.apiURL + '/all');
  }

  addCar(car: Car): Observable<Car> {
    return this.http.post<Car>(this.apiURL, car, httpOptions);
  }

  deleteCar(id: number): Observable<any> {
    const url = `${this.apiURL}/${id}`;
    return this.http.delete(url, httpOptions);
  }

  consultCar(id: number): Observable<Car> {
    const url = `${this.apiURL}/${id}`;
    return this.http.get<Car>(url);
  }

  updateCar(car: Car): Observable<Car> {
    return this.http.put<Car>(this.apiURL, car, httpOptions);
  }

  brandList(): Observable<Brand[]> {
    return this.http.get<Brand[]>(this.apiURL + '/brands');
  }

  filterCarsByBrand(idBrand: number): Observable<Car[]> {
    const url = `${this.apiURL}/carsbrand/${idBrand}`;
    return this.http.get<Car[]>(url);
  }

  searchCarsByModel(model: string): Observable<Car[]> {
    const url = `${this.apiURL}/carsbymodel/${model}`;
    return this.http.get<Car[]>(url);
  }
}

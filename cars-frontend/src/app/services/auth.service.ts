import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { JwtHelperService } from '@auth0/angular-jwt';
import { User } from '../model/user.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private helper = new JwtHelperService();
  private token: string | null = null;

  loggedUser: string = '';
  isloggedIn: boolean = false;
  roles: string[] = [];

  constructor(private http: HttpClient, private router: Router) {
    // Restore the session after a page refresh.
    const saved = localStorage.getItem('jwt');
    try {
      if (saved && !this.helper.isTokenExpired(saved)) {
        this.useToken(saved);
        return;
      }
    } catch {
      // A malformed token cannot be decoded, so it is dropped like an expired one.
    }
    localStorage.removeItem('jwt');
  }

  // The token is in a response header, so we ask for the full response.
  login(user: User): Observable<HttpResponse<void>> {
    return this.http.post<void>(environment.authURL + '/login', user, { observe: 'response' });
  }

  saveToken(jwt: string): void {
    localStorage.setItem('jwt', jwt);
    this.useToken(jwt);
  }

  getToken(): string | null {
    return this.token;
  }

  isTokenExpired(): boolean {
    return !this.token || this.helper.isTokenExpired(this.token);
  }

  logout(): void {
    this.token = null;
    this.loggedUser = '';
    this.roles = [];
    this.isloggedIn = false;
    localStorage.removeItem('jwt');
    this.router.navigate(['/login']);
  }

  isAdmin(): boolean {
    return this.roles.includes('ADMIN');
  }

  register(user: User): Observable<User> {
    return this.http.post<User>(environment.authURL + '/register', user);
  }

  verifyEmail(code: string): Observable<User> {
    return this.http.get<User>(`${environment.authURL}/verifyEmail/${encodeURIComponent(code)}`);
  }

  private useToken(jwt: string): void {
    const decoded = this.helper.decodeToken(jwt);
    this.token = jwt;
    this.loggedUser = decoded.sub;
    this.roles = decoded.roles ?? [];
    this.isloggedIn = true;
  }
}

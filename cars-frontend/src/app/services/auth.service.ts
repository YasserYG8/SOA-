import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../model/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  // Section 16 only: hard-coded users that run in the browser, so this is not real security.
  users: User[] = [
    { username: 'admin', password: '123', roles: ['ADMIN'] },
    { username: 'yasser', password: '123', roles: ['USER'] }
  ];

  loggedUser: string = '';
  isloggedIn: boolean = false;
  roles: string[] = [];

  constructor(private router: Router) {
    // Restore the session after a page refresh.
    const savedUser = localStorage.getItem('loggedUser');
    if (savedUser && localStorage.getItem('isloggedIn') === 'true') {
      this.setLoggedUser(savedUser);
    }
  }

  SignIn(user: User): boolean {
    const match = this.users.find(u => u.username === user.username && u.password === user.password);
    if (!match) {
      return false;
    }
    this.setLoggedUser(match.username);
    localStorage.setItem('loggedUser', match.username);
    localStorage.setItem('isloggedIn', 'true');
    return true;
  }

  logout(): void {
    this.loggedUser = '';
    this.roles = [];
    this.isloggedIn = false;
    localStorage.removeItem('loggedUser');
    localStorage.removeItem('isloggedIn');
    this.router.navigate(['/login']);
  }

  isAdmin(): boolean {
    return this.roles.includes('ADMIN');
  }

  private setLoggedUser(username: string): void {
    this.loggedUser = username;
    this.isloggedIn = true;
    this.roles = this.users.find(u => u.username === username)?.roles ?? [];
  }
}

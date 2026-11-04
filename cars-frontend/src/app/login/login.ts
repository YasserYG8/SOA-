import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from '../model/user.model';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.html',
})
export class LoginComponent implements OnInit {

  user = new User();
  errorMessage: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    if (!this.authService.isTokenExpired()) {
      this.router.navigate(['/']);
    }
  }

  onLoggedin(): void {
    this.errorMessage = '';
    this.authService.login(this.user).subscribe({
      next: (response) => {
        this.authService.saveToken(response.headers.get('Authorization')!);
        this.router.navigate(['/']);
      },
      error: (err) => {
        this.errorMessage = err.error?.message ?? 'Could not reach the login service. Please retry.';
      }
    });
  }
}

import { Component } from '@angular/core';
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
export class LoginComponent {

  user = new User();
  errorMessage: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  onLoggedin(): void {
    if (this.authService.SignIn(this.user)) {
      this.router.navigate(['/']);
    } else {
      this.errorMessage = 'Wrong username or password.';
    }
  }
}

import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-verify-email',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './verify-email.html',
})
export class VerifyEmailComponent implements OnInit {

  code: string = '';
  email: string = '';
  errorMessage: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.email = history.state?.email ?? '';
  }

  onVerify(): void {
    this.errorMessage = '';
    this.authService.verifyEmail(this.code.trim()).subscribe({
      next: () => this.router.navigate(['/login'], { state: { message: 'Email verified. You can log in now.' } }),
      error: (err) => {
        this.errorMessage = err.error?.message ?? 'Could not verify the code. Please retry.';
      }
    });
  }
}

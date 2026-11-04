import { Component } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../services/auth.service';

function passwordsMatch(group: AbstractControl): ValidationErrors | null {
  return group.get('password')?.value === group.get('confirmPassword')?.value ? null : { passwordsDiffer: true };
}

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './register.html',
})
export class RegisterComponent {

  errorMessage: string = '';
  isSubmitting: boolean = false;

  form = new FormGroup({
    username: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.minLength(3)] }),
    email: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.email] }),
    password: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.minLength(6)] }),
    confirmPassword: new FormControl('', { nonNullable: true, validators: [Validators.required] })
  }, { validators: passwordsMatch });

  constructor(private authService: AuthService, private router: Router) {}

  showError(field: string): boolean {
    const control = this.form.get(field)!;
    return control.invalid && control.touched;
  }

  onRegister(): void {
    const { username, email, password } = this.form.getRawValue();
    this.errorMessage = '';
    this.isSubmitting = true;
    this.authService.register({ username, email, password }).subscribe({
      next: () => this.router.navigate(['/verifyEmail'], { state: { email } }),
      error: (err) => {
        this.isSubmitting = false;
        this.errorMessage = err.error?.message ?? 'Could not create the account. Please retry.';
      }
    });
  }
}

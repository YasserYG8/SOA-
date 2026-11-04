import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-forbidden',
  standalone: true,
  imports: [RouterLink],
  template: `
    <div class="max-w-md mx-auto px-4 sm:px-6 py-16 text-center">
      <h1 class="text-2xl font-extrabold tracking-tight text-slate-900">Access denied</h1>
      <p class="text-sm text-slate-500 mt-2 mb-6">This page is for administrators. Log in with an admin account to use it.</p>
      <a routerLink="/cars" class="text-sm font-semibold text-blue-600 hover:underline">Back to the fleet</a>
    </div>
  `,
})
export class ForbiddenComponent {}

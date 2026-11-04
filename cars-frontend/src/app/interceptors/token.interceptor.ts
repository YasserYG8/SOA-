import { inject } from '@angular/core';
import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';

// These routes work without a token, and nobody is logged in yet when they are called.
const PUBLIC_URLS = ['/login', '/register', '/verifyEmail'];

export const tokenInterceptor: HttpInterceptorFn = (req, next) => {
  if (PUBLIC_URLS.some(url => req.url.includes(url))) {
    return next(req);
  }

  const authService = inject(AuthService);
  const jwt = authService.getToken();
  const request = jwt ? req.clone({ setHeaders: { Authorization: 'Bearer ' + jwt } }) : req;

  return next(request).pipe(
    catchError((err: HttpErrorResponse) => {
      // The server refused the token, for example because it expired during the session.
      if (err.status === 401) {
        authService.logout();
      }
      return throwError(() => err);
    })
  );
};

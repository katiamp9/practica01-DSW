import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';

import { AuthService } from '../servicios/auth.service';

export const authInterceptor: HttpInterceptorFn = (request, next) => {
  const authService = inject(AuthService);

  const isLoginRequest = request.url.endsWith('/api/v1/auth/login');
  if (isLoginRequest) {
    return next(request);
  }

  if (!authService.isAuthenticated()) {
    return next(request);
  }

  const basicToken = authService.basicAuthToken();
  if (!basicToken) {
    return next(request);
  }

  const authorizedRequest = request.clone({
    setHeaders: {
      Authorization: `Basic ${basicToken}`
    }
  });

  return next(authorizedRequest);
};

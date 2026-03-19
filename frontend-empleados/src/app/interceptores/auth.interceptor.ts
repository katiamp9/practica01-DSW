import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (request, next) => {
  const basicToken = localStorage.getItem('basic_auth_token');

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

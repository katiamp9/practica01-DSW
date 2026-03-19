import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

import { AppRole } from '../modelos/app-roles';
import { AuthService } from '../servicios/auth.service';

export const roleGuard: CanActivateFn = (route) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const expectedRole = route.data['role'] as AppRole | undefined;

  if (!authService.isAuthenticated()) {
    authService.clearSession();
    return router.createUrlTree(['/login']);
  }

  if (!expectedRole) {
    return true;
  }

  if (authService.canAccessRole(expectedRole)) {
    return true;
  }

  authService.clearSession();
  return router.createUrlTree(['/login']);
};
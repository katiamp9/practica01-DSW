import { inject, Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';

import { APP_ROLES, AppRole, ALL_ROLES } from '../modelos/app-roles';
import { AuthLoginResponse } from '../modelos/auth-login.model';
import { AuthSession } from '../modelos/auth-session.model';
import { SESSION_STORAGE_KEY } from '../modelos/session.constants';

const EMPTY_SESSION: AuthSession = {
  isAuthenticated: false,
  basicAuthToken: '',
  role: '',
  email: '',
  nombre: ''
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly router = inject(Router);

  readonly session = signal<AuthSession>(EMPTY_SESSION);

  constructor() {
    this.restoreSession();
  }

  isAuthenticated(): boolean {
    return this.session().isAuthenticated;
  }

  role(): string {
    return this.session().role;
  }

  email(): string {
    return this.session().email;
  }

  nombre(): string {
    return this.session().nombre;
  }

  basicAuthToken(): string {
    return this.session().basicAuthToken;
  }

  loginSuccess(email: string, password: string, response: AuthLoginResponse): boolean {
    const role = this.normalizeRole(response.rol);
    if (!role) {
      this.clearSessionAndRedirect();
      return false;
    }

    const nextSession: AuthSession = {
      isAuthenticated: true,
      basicAuthToken: btoa(`${email}:${password}`),
      role,
      email,
      nombre: response.nombre,
    };

    this.session.set(nextSession);
    localStorage.setItem(SESSION_STORAGE_KEY, JSON.stringify(nextSession));
    return true;
  }

  logout(): void {
    this.clearSessionAndRedirect();
  }

  clearSession(): void {
    this.session.set(EMPTY_SESSION);
    localStorage.removeItem(SESSION_STORAGE_KEY);
  }

  clearSessionAndRedirect(): void {
    this.clearSession();
    this.router.navigate(['/login']);
  }

  canAccessRole(expectedRole: AppRole): boolean {
    return this.isAuthenticated() && this.role() === expectedRole;
  }

  defaultRouteByRole(): string {
    return this.role() === APP_ROLES.ADMIN ? '/admin-dashboard' : '/home';
  }

  private restoreSession(): void {
    const raw = localStorage.getItem(SESSION_STORAGE_KEY);
    if (!raw) {
      return;
    }

    try {
      const stored = JSON.parse(raw) as AuthSession;
      if (!stored.isAuthenticated || !stored.basicAuthToken) {
        this.clearSessionAndRedirect();
        return;
      }

      const normalizedRole = this.normalizeRole(stored.role);
      if (!normalizedRole) {
        this.clearSessionAndRedirect();
        return;
      }

      this.session.set({
        isAuthenticated: true,
        basicAuthToken: stored.basicAuthToken,
        role: normalizedRole,
        email: stored.email,
        nombre: stored.nombre,
      });
    } catch {
      this.clearSessionAndRedirect();
    }
  }

  private normalizeRole(role: string): AppRole | null {
    if (!role) {
      return null;
    }

    const normalized = role.trim().toUpperCase();
    if (ALL_ROLES.includes(normalized as AppRole)) {
      return normalized as AppRole;
    }

    if (normalized === 'ADMIN') {
      return APP_ROLES.ADMIN;
    }

    if (normalized === 'USER') {
      return APP_ROLES.USER;
    }

    return null;
  }
}

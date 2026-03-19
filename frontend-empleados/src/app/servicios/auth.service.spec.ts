/// <reference types="jasmine" />

import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { APP_ROLES } from '../modelos/app-roles';
import { SESSION_STORAGE_KEY } from '../modelos/session.constants';
import { AuthService } from './auth.service';

describe('AuthService', () => {
  let service: AuthService;

  beforeEach(() => {
    localStorage.clear();
    TestBed.configureTestingModule({
      providers: [AuthService, provideRouter([])]
    });
    service = TestBed.inject(AuthService);
  });

  it('should persist authenticated session on valid login', () => {
    const result = service.loginSuccess('admin@empresa.com', '1234', {
      rol: APP_ROLES.ADMIN,
      nombre: 'Admin'
    });

    expect(result).toBeTrue();
    expect(service.isAuthenticated()).toBeTrue();
    expect(service.role()).toBe(APP_ROLES.ADMIN);
    expect(localStorage.getItem(SESSION_STORAGE_KEY)).toBeTruthy();
  });

  it('should reject unknown role and keep user unauthenticated', () => {
    const result = service.loginSuccess('admin@empresa.com', '1234', {
      rol: 'ROLE_UNKNOWN',
      nombre: 'X'
    });

    expect(result).toBeFalse();
    expect(service.isAuthenticated()).toBeFalse();
  });

  it('should clear session and local storage', () => {
    service.loginSuccess('user@empresa.com', '1234', {
      rol: APP_ROLES.USER,
      nombre: 'User'
    });

    service.clearSession();

    expect(service.isAuthenticated()).toBeFalse();
    expect(localStorage.getItem(SESSION_STORAGE_KEY)).toBeNull();
  });
});
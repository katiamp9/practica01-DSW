/// <reference types="jasmine" />

import { TestBed } from '@angular/core/testing';
import { Router, UrlTree } from '@angular/router';
import { provideRouter } from '@angular/router';

import { authGuard } from './auth.guard';
import { AuthService } from '../servicios/auth.service';

describe('authGuard', () => {
  it('should allow route when user is authenticated', () => {
    const authServiceMock = {
      isAuthenticated: jasmine.createSpy().and.returnValue(true),
      clearSession: jasmine.createSpy()
    };

    TestBed.configureTestingModule({
      providers: [
        provideRouter([]),
        { provide: AuthService, useValue: authServiceMock }
      ]
    });

    const result = TestBed.runInInjectionContext(() => authGuard({} as never, {} as never));

    expect(result).toBeTrue();
  });

  it('should redirect to login when user is not authenticated', () => {
    const authServiceMock = {
      isAuthenticated: jasmine.createSpy().and.returnValue(false),
      clearSession: jasmine.createSpy()
    };

    TestBed.configureTestingModule({
      providers: [
        provideRouter([]),
        { provide: AuthService, useValue: authServiceMock }
      ]
    });

    const result = TestBed.runInInjectionContext(() => authGuard({} as never, {} as never));
    const router = TestBed.inject(Router);

    expect(result instanceof UrlTree).toBeTrue();
    expect(router.serializeUrl(result as UrlTree)).toContain('/login');
    expect(authServiceMock.clearSession).toHaveBeenCalled();
  });
});
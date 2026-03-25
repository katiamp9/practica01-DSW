/// <reference types="jasmine" />

import { HttpRequest, HttpResponse } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';

import { authInterceptor } from './auth.interceptor';
import { AuthService } from '../servicios/auth.service';

describe('authInterceptor', () => {
  it('should add basic authorization header for authenticated requests', (done: DoneFn) => {
    const authServiceMock = {
      isAuthenticated: jasmine.createSpy().and.returnValue(true),
      basicAuthToken: jasmine.createSpy().and.returnValue('abc123')
    };

    TestBed.configureTestingModule({
      providers: [{ provide: AuthService, useValue: authServiceMock }]
    });

    const request = new HttpRequest('GET', '/api/v1/departamentos');

    TestBed.runInInjectionContext(() => {
      authInterceptor(request, (nextRequest) => {
        expect(nextRequest.headers.get('Authorization')).toBe('Basic abc123');
        return of(new HttpResponse({ status: 200 }));
      }).subscribe(() => {
        done();
      });
    });
  });

  it('should not add authorization header on login endpoint', (done: DoneFn) => {
    const authServiceMock = {
      isAuthenticated: jasmine.createSpy().and.returnValue(true),
      basicAuthToken: jasmine.createSpy().and.returnValue('abc123')
    };

    TestBed.configureTestingModule({
      providers: [{ provide: AuthService, useValue: authServiceMock }]
    });

    const request = new HttpRequest('POST', '/api/v1/auth/login', {});

    TestBed.runInInjectionContext(() => {
      authInterceptor(request, (nextRequest) => {
        expect(nextRequest.headers.has('Authorization')).toBeFalse();
        return of(new HttpResponse({ status: 200 }));
      }).subscribe(() => {
        done();
      });
    });
  });
});
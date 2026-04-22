/// <reference types="jasmine" />

import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { authInterceptor } from '../interceptores/auth.interceptor';
import { AuthService } from './auth.service';
import { DepartamentoService } from './departamento.service';

describe('DepartamentoService', () => {
  let service: DepartamentoService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    const authServiceMock = {
      isAuthenticated: jasmine.createSpy().and.returnValue(true),
      basicAuthToken: jasmine.createSpy().and.returnValue('admin-token')
    };

    TestBed.configureTestingModule({
      providers: [
        DepartamentoService,
        { provide: AuthService, useValue: authServiceMock },
        provideHttpClient(withInterceptors([authInterceptor])),
        provideHttpClientTesting()
      ]
    });

    service = TestBed.inject(DepartamentoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should request departamentos list with pageable params', () => {
    service.list(0).subscribe((response) => {
      expect(response.content.length).toBe(1);
    });

    const request = httpMock.expectOne((req) =>
      req.method === 'GET'
      && req.url === '/api/v1/departamentos'
      && req.params.get('page') === '0'
      && req.params.get('size') === '5'
      && req.params.get('sort') === 'nombre,asc'
    );

    request.flush({
      content: [{ id: 1, nombre: 'Sistemas', totalEmpleados: 3 }],
      number: 0,
      size: 5,
      totalElements: 1,
      totalPages: 1,
      first: true,
      last: true
    });
  });

  it('should call create/update/delete endpoints', () => {
    service.create({ nombre: 'Finanzas' }).subscribe();

    const createReq = httpMock.expectOne('/api/v1/departamentos');
    expect(createReq.request.method).toBe('POST');
    createReq.flush({ id: 10, nombre: 'Finanzas', totalEmpleados: 0 });

    service.update(10, { nombre: 'Finanzas TI' }).subscribe();

    const updateReq = httpMock.expectOne('/api/v1/departamentos/10');
    expect(updateReq.request.method).toBe('PUT');
    updateReq.flush({ id: 10, nombre: 'Finanzas TI', totalEmpleados: 0 });

    service.delete(10).subscribe((response) => {
      expect(response).toBeNull();
    });

    const deleteReq = httpMock.expectOne('/api/v1/departamentos/10');
    expect(deleteReq.request.method).toBe('DELETE');
    deleteReq.flush(null);
  });

  it('should include basic authorization header for protected departamentos requests', () => {
    service.list(0).subscribe();

    const request = httpMock.expectOne('/api/v1/departamentos?page=0&size=5&sort=nombre,asc');
    expect(request.request.headers.get('Authorization')).toBe('Basic admin-token');

    request.flush({
      content: [],
      number: 0,
      size: 5,
      totalElements: 0,
      totalPages: 0,
      first: true,
      last: true
    });
  });
});

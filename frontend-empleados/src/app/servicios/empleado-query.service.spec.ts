/// <reference types="jasmine" />

import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { EmpleadoQueryService } from './empleado-query.service';

describe('EmpleadoQueryService', () => {
  let service: EmpleadoQueryService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        EmpleadoQueryService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });

    service = TestBed.inject(EmpleadoQueryService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should request paginated empleados using HttpClient with required params', () => {
    service.list(0, 10, 'nombre,asc').subscribe((response) => {
      expect(response.content.length).toBe(1);
      expect(response.number).toBe(0);
      expect(response.size).toBe(10);
    });

    const request = httpMock.expectOne((req) =>
      req.method === 'GET'
      && req.url === '/api/v1/empleados'
      && req.params.get('page') === '0'
      && req.params.get('size') === '10'
      && req.params.get('sort') === 'nombre,asc'
    );

    request.flush({
      content: [{ clave: 'EMP-001', nombre: 'Ana', rol: 'ROLE_ADMIN' }],
      number: 0,
      size: 10,
      totalElements: 1,
      totalPages: 1,
      first: true,
      last: true
    });
  });
});

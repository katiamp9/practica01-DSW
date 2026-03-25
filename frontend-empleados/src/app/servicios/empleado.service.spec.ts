/// <reference types="jasmine" />

import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { EmpleadoService } from './empleado.service';

describe('EmpleadoService', () => {
  let service: EmpleadoService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        EmpleadoService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });

    service = TestBed.inject(EmpleadoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should request empleados list with pageable params', () => {
    service.list(0, 10, 'nombre,asc').subscribe((response) => {
      expect(response.content.length).toBe(1);
    });

    const request = httpMock.expectOne((req) =>
      req.method === 'GET'
      && req.url === '/api/v1/empleados'
      && req.params.get('page') === '0'
      && req.params.get('size') === '10'
      && req.params.get('sort') === 'nombre,asc'
    );

    request.flush({
      content: [{ clave: 'EMP-001', nombre: 'Ana', email: 'ana@empresa.com', departamentoId: 1 }],
      number: 0,
      size: 10,
      totalElements: 1,
      totalPages: 1,
      first: true,
      last: true
    });
  });

  it('should request departamentos with pageable params', () => {
    service.listDepartamentos(0, 20, 'nombre,asc').subscribe((response) => {
      expect(response.content[0].nombre).toBe('TI');
    });

    const request = httpMock.expectOne((req) =>
      req.method === 'GET'
      && req.url === '/api/v1/departamentos'
      && req.params.get('page') === '0'
      && req.params.get('size') === '20'
      && req.params.get('sort') === 'nombre,asc'
    );

    request.flush({
      content: [{ id: 1, nombre: 'TI' }],
      number: 0,
      size: 20,
      totalElements: 1,
      totalPages: 1,
      first: true,
      last: true
    });
  });

  it('should call create/update/delete endpoints', () => {
    service.create({
      nombre: 'Ana',
      email: 'ana@empresa.com',
      departamentoId: 1
    }).subscribe();

    const createReq = httpMock.expectOne('/api/v1/empleados');
    expect(createReq.request.method).toBe('POST');
    createReq.flush({ clave: 'EMP-001', nombre: 'Ana', email: 'ana@empresa.com', departamentoId: 1 });

    service.update('EMP-001', {
      nombre: 'Ana M',
      email: 'ana@empresa.com',
      departamentoId: 1
    }).subscribe();

    const updateReq = httpMock.expectOne('/api/v1/empleados/EMP-001');
    expect(updateReq.request.method).toBe('PUT');
    updateReq.flush({ clave: 'EMP-001', nombre: 'Ana M', email: 'ana@empresa.com', departamentoId: 1 });

    service.delete('EMP-001').subscribe((response) => {
      expect(response).toBeNull();
    });

    const deleteReq = httpMock.expectOne('/api/v1/empleados/EMP-001');
    expect(deleteReq.request.method).toBe('DELETE');
    deleteReq.flush(null);
  });
});

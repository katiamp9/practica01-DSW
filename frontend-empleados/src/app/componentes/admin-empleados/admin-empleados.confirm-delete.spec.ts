/// <reference types="jasmine" />

import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';

import { AdminEmpleadosComponent } from './admin-empleados.component';
import { AuthService } from '../../servicios/auth.service';
import { EmpleadoService } from '../../servicios/empleado.service';

describe('AdminEmpleadosComponent confirm delete flow', () => {
  const authMock = {
    nombre: jasmine.createSpy().and.returnValue('Admin'),
    email: jasmine.createSpy().and.returnValue('admin@empresa.com'),
    logout: jasmine.createSpy()
  };

  const empleadoServiceMock = {
    list: jasmine.createSpy(),
    listDepartamentos: jasmine.createSpy(),
    create: jasmine.createSpy(),
    update: jasmine.createSpy(),
    delete: jasmine.createSpy().and.returnValue(of(undefined))
  };

  beforeEach(() => {
    empleadoServiceMock.list.calls.reset();
    empleadoServiceMock.listDepartamentos.calls.reset();
    empleadoServiceMock.create.calls.reset();
    empleadoServiceMock.update.calls.reset();
    empleadoServiceMock.delete.calls.reset();

    empleadoServiceMock.list.and.returnValue(of({
      content: [{ clave: 'EMP-001', nombre: 'Ana', email: 'ana@empresa.com', departamentoId: 1 }],
      number: 0,
      size: 10,
      totalElements: 1,
      totalPages: 1,
      first: true,
      last: true
    }));

    empleadoServiceMock.listDepartamentos.and.returnValue(of({
      content: [{ id: 1, nombre: 'TI' }],
      number: 0,
      size: 100,
      totalElements: 1,
      totalPages: 1,
      first: true,
      last: true
    }));

    TestBed.configureTestingModule({
      imports: [AdminEmpleadosComponent],
      providers: [
        provideRouter([]),
        { provide: AuthService, useValue: authMock },
        { provide: EmpleadoService, useValue: empleadoServiceMock }
      ]
    });
  });

  it('should request and confirm deletion', () => {
    const fixture = TestBed.createComponent(AdminEmpleadosComponent);
    fixture.detectChanges();

    const component = fixture.componentInstance;
    component.requestDelete({ clave: 'EMP-001', nombre: 'Ana', email: 'ana@empresa.com', departamentoId: 1 });

    expect(component.confirmacion().visible).toBeTrue();

    component.confirmDelete();

    expect(empleadoServiceMock.delete).toHaveBeenCalledWith('EMP-001');
    expect(component.confirmacion().visible).toBeFalse();
  });

  it('should cancel deletion without invoking service', () => {
    const fixture = TestBed.createComponent(AdminEmpleadosComponent);
    fixture.detectChanges();

    const component = fixture.componentInstance;
    component.requestDelete({ clave: 'EMP-001', nombre: 'Ana', email: 'ana@empresa.com', departamentoId: 1 });
    component.cancelDelete();

    expect(component.confirmacion().visible).toBeFalse();
    expect(empleadoServiceMock.delete).not.toHaveBeenCalled();
  });
});

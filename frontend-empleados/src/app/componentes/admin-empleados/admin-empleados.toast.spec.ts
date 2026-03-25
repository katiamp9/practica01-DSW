/// <reference types="jasmine" />

import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of, throwError } from 'rxjs';

import { AdminEmpleadosComponent } from './admin-empleados.component';
import { AuthService } from '../../servicios/auth.service';
import { EmpleadoService } from '../../servicios/empleado.service';

describe('AdminEmpleadosComponent toast feedback', () => {
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
    delete: jasmine.createSpy()
  };

  beforeEach(() => {
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

    empleadoServiceMock.update.and.returnValue(of({
      clave: 'EMP-001',
      nombre: 'Ana 2',
      email: 'ana@empresa.com',
      departamentoId: 1
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

  it('should show success toast after update', () => {
    const fixture = TestBed.createComponent(AdminEmpleadosComponent);
    fixture.detectChanges();

    const component = fixture.componentInstance;
    component.openEditForm({ clave: 'EMP-001', nombre: 'Ana', email: 'ana@empresa.com', departamentoId: 1 });
    component.submitForm({
      nombre: 'Ana 2',
      email: 'ana@empresa.com',
      departamentoId: 1
    });

    expect(empleadoServiceMock.update).toHaveBeenCalled();
    expect(component.toastMessage()).toContain('actualizado');

    component.dismissToast();
    expect(component.toastMessage()).toBeNull();
  });

  it('should show error toast when delete fails', () => {
    empleadoServiceMock.delete.and.returnValue(throwError(() => new Error('delete failed')));

    const fixture = TestBed.createComponent(AdminEmpleadosComponent);
    fixture.detectChanges();

    const component = fixture.componentInstance;
    component.requestDelete({ clave: 'EMP-001', nombre: 'Ana', email: 'ana@empresa.com', departamentoId: 1 });
    component.confirmDelete();

    expect(component.toastVariant()).toBe('error');
    expect(component.toastMessage()).toContain('No se pudo eliminar');
  });
});

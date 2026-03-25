/// <reference types="jasmine" />

import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';

import { AdminEmpleadosComponent } from './admin-empleados.component';
import { AuthService } from '../../servicios/auth.service';
import { EmpleadoService } from '../../servicios/empleado.service';

describe('AdminEmpleadosComponent CRUD flow', () => {
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

    empleadoServiceMock.create.and.returnValue(of({
      clave: 'EMP-002',
      nombre: 'Luis',
      email: 'luis@empresa.com',
      departamentoId: 1
    }));

    empleadoServiceMock.update.and.returnValue(of({
      clave: 'EMP-001',
      nombre: 'Ana María',
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

  it('should execute create flow and show success toast', () => {
    const fixture = TestBed.createComponent(AdminEmpleadosComponent);
    fixture.detectChanges();

    const initialListCalls = empleadoServiceMock.list.calls.count();

    const component = fixture.componentInstance;
    component.openCreateForm();

    component.submitForm({
      nombre: 'Luis',
      email: 'luis@empresa.com',
      departamentoId: 1
    });

    expect(empleadoServiceMock.create).toHaveBeenCalled();
    const payload = empleadoServiceMock.create.calls.mostRecent().args[0];
    expect('clave' in payload).toBeFalse();
    expect(component.toastMessage()).toContain('creado');
    expect(empleadoServiceMock.list.calls.count()).toBeGreaterThan(initialListCalls);
  });

  it('should execute update flow using empleado clave and payload with departamentoId', () => {
    const fixture = TestBed.createComponent(AdminEmpleadosComponent);
    fixture.detectChanges();

    const component = fixture.componentInstance;
    component.openEditForm({
      clave: 'EMP-001',
      nombre: 'Ana',
      email: 'ana@empresa.com',
      departamentoId: 1
    });

    component.submitForm({
      nombre: 'Ana María',
      email: 'ana@empresa.com',
      departamentoId: 1
    });

    expect(empleadoServiceMock.update).toHaveBeenCalledWith('EMP-001', jasmine.objectContaining({
      nombre: 'Ana María',
      departamentoId: 1
    }));
    expect(component.toastMessage()).toContain('actualizado');
  });
});

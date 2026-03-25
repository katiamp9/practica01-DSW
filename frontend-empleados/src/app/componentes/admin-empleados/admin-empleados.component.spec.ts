/// <reference types="jasmine" />

import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';

import { AdminEmpleadosComponent } from './admin-empleados.component';
import { AuthService } from '../../servicios/auth.service';
import { EmpleadoService } from '../../servicios/empleado.service';

describe('AdminEmpleadosComponent', () => {
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

    TestBed.configureTestingModule({
      imports: [AdminEmpleadosComponent],
      providers: [
        provideRouter([]),
        { provide: AuthService, useValue: authMock },
        { provide: EmpleadoService, useValue: empleadoServiceMock }
      ]
    });
  });

  it('should load empleados on init and open create form loading departamentos', () => {
    const fixture = TestBed.createComponent(AdminEmpleadosComponent);
    fixture.detectChanges();

    const component = fixture.componentInstance;

    expect(component.status()).toBe('data');
    expect(component.empleados().length).toBe(1);
    expect(empleadoServiceMock.listDepartamentos).toHaveBeenCalled();

    component.openCreateForm();

    expect(component.formVisible()).toBeTrue();
    expect(empleadoServiceMock.listDepartamentos).toHaveBeenCalled();
  });

  it('should resolve departamento name from catalog and return fallback when missing', () => {
    const fixture = TestBed.createComponent(AdminEmpleadosComponent);
    fixture.detectChanges();

    const component = fixture.componentInstance;

    expect(component.getDepartamentoNombre(1)).toBe('TI');
    expect(component.getDepartamentoNombre(999)).toBe('Sin asignar');
    expect(component.getDepartamentoNombre(null)).toBe('Sin asignar');
  });

  it('should hide delete action when empleado email matches authenticated email', () => {
    authMock.email.and.returnValue('ana@empresa.com');

    const fixture = TestBed.createComponent(AdminEmpleadosComponent);
    fixture.detectChanges();

    const deleteButton = fixture.nativeElement.querySelector('button.btn-danger');
    expect(deleteButton).toBeNull();
  });

  it('should render "Sin correo" when empleado email is null', () => {
    empleadoServiceMock.list.and.returnValue(of({
      content: [{ clave: 'EMP-002', nombre: 'Luis', email: null, departamentoId: 1 }],
      number: 0,
      size: 10,
      totalElements: 1,
      totalPages: 1,
      first: true,
      last: true
    }));

    const fixture = TestBed.createComponent(AdminEmpleadosComponent);
    fixture.detectChanges();

    expect(fixture.nativeElement.textContent).toContain('Sin correo');
  });
});

/// <reference types="jasmine" />

import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { AdminDashboardComponent } from './admin-dashboard.component';
import { EmpleadoQueryService } from '../../servicios/empleado-query.service';
import { AuthService } from '../../servicios/auth.service';
import { of, throwError } from 'rxjs';

describe('AdminDashboardComponent', () => {
  const authServiceMock = {
    nombre: jasmine.createSpy().and.returnValue('Admin Test'),
    logout: jasmine.createSpy()
  };

  let empleadoQueryServiceMock: { list: jasmine.Spy };

  beforeEach(() => {
    empleadoQueryServiceMock = {
      list: jasmine.createSpy()
    };
  });

  it('should set data state when empleados are returned', () => {
    empleadoQueryServiceMock.list.and.returnValue(of({
      content: [{ clave: 'EMP-001', nombre: 'Ana', rol: 'ROLE_ADMIN' }],
      number: 0,
      size: 10,
      totalElements: 1,
      totalPages: 1,
      first: true,
      last: true
    }));

    TestBed.configureTestingModule({
      imports: [AdminDashboardComponent],
      providers: [
        provideRouter([]),
        { provide: AuthService, useValue: authServiceMock },
        { provide: EmpleadoQueryService, useValue: empleadoQueryServiceMock }
      ]
    });

    const fixture = TestBed.createComponent(AdminDashboardComponent);
    fixture.detectChanges();

    const component = fixture.componentInstance;
    expect(component.status()).toBe('data');
    expect(component.empleados().length).toBe(1);
  });

  it('should set empty state with friendly message when no empleados exist', () => {
    empleadoQueryServiceMock.list.and.returnValue(of({
      content: [],
      number: 0,
      size: 10,
      totalElements: 0,
      totalPages: 0,
      first: true,
      last: true
    }));

    TestBed.configureTestingModule({
      imports: [AdminDashboardComponent],
      providers: [
        provideRouter([]),
        { provide: AuthService, useValue: authServiceMock },
        { provide: EmpleadoQueryService, useValue: empleadoQueryServiceMock }
      ]
    });

    const fixture = TestBed.createComponent(AdminDashboardComponent);
    fixture.detectChanges();

    const component = fixture.componentInstance;
    expect(component.status()).toBe('empty');
    expect(component.emptyMessage()).toContain('No hay empleados');
  });

  it('should set error state and retry should request again', () => {
    empleadoQueryServiceMock.list.and.returnValues(
      throwError(() => new Error('network')),
      of({
        content: [{ clave: 'EMP-001', nombre: 'Ana', rol: 'ROLE_ADMIN' }],
        number: 0,
        size: 10,
        totalElements: 1,
        totalPages: 1,
        first: true,
        last: true
      })
    );

    TestBed.configureTestingModule({
      imports: [AdminDashboardComponent],
      providers: [
        provideRouter([]),
        { provide: AuthService, useValue: authServiceMock },
        { provide: EmpleadoQueryService, useValue: empleadoQueryServiceMock }
      ]
    });

    const fixture = TestBed.createComponent(AdminDashboardComponent);
    fixture.detectChanges();

    const component = fixture.componentInstance;
    expect(component.status()).toBe('error');

    component.retry();

    expect(empleadoQueryServiceMock.list).toHaveBeenCalledTimes(2);
    expect(component.status()).toBe('data');
  });
});

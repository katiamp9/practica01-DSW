/// <reference types="jasmine" />

import { SimpleChange } from '@angular/core';
import { TestBed } from '@angular/core/testing';

import { EmpleadoFormComponent } from './empleado-form.component';

describe('EmpleadoFormComponent', () => {
  it('should emit payload without clave on valid create submit', () => {
    TestBed.configureTestingModule({
      imports: [EmpleadoFormComponent]
    });

    const fixture = TestBed.createComponent(EmpleadoFormComponent);
    const component = fixture.componentInstance;

    component.departamentos = [{ id: 1, nombre: 'TI' }];
    fixture.detectChanges();

    component.form.setValue({
      clave: '',
      nombre: 'Ana',
      email: 'ana@empresa.com',
      direccion: 'Calle 1',
      telefono: '123456',
      departamentoId: 1,
      password: 'abc123'
    });

    const submitSpy = jasmine.createSpy('submitted');
    component.submitted.subscribe(submitSpy);

    component.onSubmit();

    expect(submitSpy).toHaveBeenCalled();
    const payload = submitSpy.calls.mostRecent().args[0];
    expect(payload.departamentoId).toBe(1);
    expect('clave' in payload).toBeFalse();
  });

  it('should hide clave input in create mode', () => {
    TestBed.configureTestingModule({
      imports: [EmpleadoFormComponent]
    });

    const fixture = TestBed.createComponent(EmpleadoFormComponent);
    fixture.componentInstance.mode = 'create';
    fixture.detectChanges();

    expect(fixture.nativeElement.textContent).not.toContain('Clave *');
  });

  it('should show clave input in edit mode and preload empleado values', () => {
    TestBed.configureTestingModule({
      imports: [EmpleadoFormComponent]
    });

    const fixture = TestBed.createComponent(EmpleadoFormComponent);
    const component = fixture.componentInstance;
    component.mode = 'edit';
    component.empleado = {
      clave: 'EMP-001',
      nombre: 'Ana',
      email: 'ana@empresa.com',
      direccion: 'Calle 1',
      telefono: '123456',
      departamentoId: 1
    };
    component.departamentos = [{ id: 1, nombre: 'TI' }];
    component.ngOnChanges({
      mode: new SimpleChange('create', 'edit', false),
      empleado: new SimpleChange(null, component.empleado, false)
    });

    fixture.detectChanges();

    expect(fixture.nativeElement.textContent).toContain('Clave *');
    expect(component.form.controls.clave.disabled).toBeTrue();
    const emailInput: HTMLInputElement | null = fixture.nativeElement.querySelector('input[type="email"]');
    expect(emailInput?.readOnly).toBeTrue();
    expect(component.form.controls.nombre.value).toBe('Ana');
    expect(component.form.controls.departamentoId.value).toBe(1);
  });
});

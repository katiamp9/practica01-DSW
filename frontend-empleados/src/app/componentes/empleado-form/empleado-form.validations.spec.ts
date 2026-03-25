/// <reference types="jasmine" />

import { SimpleChange } from '@angular/core';
import { TestBed } from '@angular/core/testing';

import { EmpleadoFormComponent } from './empleado-form.component';

describe('EmpleadoFormComponent validations', () => {
  it('should block submit for non corporate email', () => {
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
      email: 'ana@gmail.com',
      direccion: '',
      telefono: '',
      departamentoId: 1,
      password: ''
    });

    const submitSpy = jasmine.createSpy('submitted');
    component.submitted.subscribe(submitSpy);

    component.onSubmit();

    expect(submitSpy).not.toHaveBeenCalled();
  });

  it('should disable departamento control and submit button when no departments warning is active', () => {
    TestBed.configureTestingModule({
      imports: [EmpleadoFormComponent]
    });

    const fixture = TestBed.createComponent(EmpleadoFormComponent);
    const component = fixture.componentInstance;
    component.showNoDepartmentsWarning = true;
    component.loadingDepartamentos = false;
    component.ngOnChanges({
      showNoDepartmentsWarning: new SimpleChange(false, true, false),
      loadingDepartamentos: new SimpleChange(true, false, false)
    });
    fixture.detectChanges();

    expect(component.form.controls.departamentoId.disabled).toBeTrue();

    const submitButton = fixture.nativeElement.querySelector('button[type="submit"]') as HTMLButtonElement;
    expect(submitButton.disabled).toBeTrue();
  });
});

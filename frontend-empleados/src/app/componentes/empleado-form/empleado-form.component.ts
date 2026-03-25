import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges, inject } from '@angular/core';
import { AbstractControl, FormBuilder, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';

import { DepartamentoOption } from '../../modelos/departamento-option.model';
import { EmpleadoCrudPayload, EmpleadoGestion } from '../../modelos/empleado-gestion.model';
import { FormMode } from '../../modelos/estado-formulario-empleado.model';
import { isCorporateEmail } from '../../modelos/empleado-validations';

@Component({
  selector: 'app-empleado-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './empleado-form.component.html',
  styleUrl: './empleado-form.component.css'
})
export class EmpleadoFormComponent implements OnChanges {
  private readonly fb = inject(FormBuilder);

  @Input() mode: FormMode = 'create';
  @Input() empleado: EmpleadoGestion | null = null;
  @Input() departamentos: DepartamentoOption[] = [];
  @Input() loadingDepartamentos = false;
  @Input() showNoDepartmentsWarning = false;
  @Input() submitting = false;
  @Input() operationError: string | null = null;

  @Output() canceled = new EventEmitter<void>();
  @Output() submitted = new EventEmitter<EmpleadoCrudPayload>();

  readonly form = this.fb.group({
    clave: this.fb.nonNullable.control(''),
    nombre: this.fb.nonNullable.control('', [Validators.required]),
    email: this.fb.nonNullable.control('', [Validators.required, Validators.email, corporateEmailValidator]),
    direccion: this.fb.nonNullable.control(''),
    telefono: this.fb.nonNullable.control(''),
    departamentoId: this.fb.control<number | null>(null, [Validators.required]),
    password: this.fb.nonNullable.control('')
  });

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['empleado'] || changes['mode']) {
      this.syncFormWithInputs();
    }

    if (changes['loadingDepartamentos'] || changes['showNoDepartmentsWarning']) {
      this.syncDepartamentoControlState();
    }
  }

  onCancel(): void {
    this.canceled.emit();
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const raw = this.form.getRawValue();

    if (raw.departamentoId === null) {
      return;
    }

    const payload: EmpleadoCrudPayload = {
      nombre: raw.nombre.trim(),
      email: raw.email.trim(),
      direccion: raw.direccion.trim() || undefined,
      telefono: raw.telefono.trim() || undefined,
      departamentoId: raw.departamentoId,
      password: raw.password.trim() || undefined
    };

    this.submitted.emit(payload);
  }

  private syncFormWithInputs(): void {
    if (this.mode === 'edit' && this.empleado) {
      this.form.reset({
        clave: this.empleado.clave,
        nombre: this.empleado.nombre,
        email: this.empleado.email ?? '',
        direccion: this.empleado.direccion ?? '',
        telefono: this.empleado.telefono ?? '',
        departamentoId: this.empleado.departamentoId,
        password: ''
      });
      this.form.controls.clave.disable();
      return;
    }

    this.form.reset({
      clave: this.empleado?.clave ?? '',
      nombre: '',
      email: '',
      direccion: '',
      telefono: '',
      departamentoId: null,
      password: ''
    });
    this.form.controls.clave.enable();
    this.syncDepartamentoControlState();
  }

  private syncDepartamentoControlState(): void {
    if (this.loadingDepartamentos || this.showNoDepartmentsWarning) {
      this.form.controls.departamentoId.disable({ emitEvent: false });
      return;
    }

    this.form.controls.departamentoId.enable({ emitEvent: false });
  }

}

function corporateEmailValidator(control: AbstractControl): ValidationErrors | null {
  const value = String(control.value ?? '').trim();
  if (!value) {
    return null;
  }

  return isCorporateEmail(value) ? null : { corporateEmail: true };
}

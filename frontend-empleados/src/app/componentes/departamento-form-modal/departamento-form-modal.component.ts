import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { DepartamentoGestion } from '../../modelos/departamento-gestion.model';
import { DepartamentoFormMode } from '../../modelos/departamento-form-state.model';

export interface DepartamentoFormPayload {
  nombre: string;
}

@Component({
  selector: 'app-departamento-form-modal',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './departamento-form-modal.component.html',
  styleUrl: './departamento-form-modal.component.css'
})
export class DepartamentoFormModalComponent implements OnChanges {
  private readonly fb = inject(FormBuilder);

  @Input() mode: DepartamentoFormMode = 'create';
  @Input() departamento: DepartamentoGestion | null = null;
  @Input() submitting = false;
  @Input() operationError: string | null = null;

  @Output() canceled = new EventEmitter<boolean>();
  @Output() submitted = new EventEmitter<DepartamentoFormPayload>();

  readonly form = this.fb.group({
    nombre: this.fb.nonNullable.control('', [Validators.required])
  });

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['mode'] || changes['departamento']) {
      if (this.mode === 'edit' && this.departamento) {
        this.form.reset({
          nombre: this.departamento.nombre
        });
        this.form.markAsPristine();
        return;
      }

      this.form.reset({
        nombre: ''
      });
      this.form.markAsPristine();
    }
  }

  onCancel(): void {
    this.canceled.emit(this.form.dirty);
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const nombre = this.form.controls.nombre.value.trim();
    this.submitted.emit({ nombre });
  }
}

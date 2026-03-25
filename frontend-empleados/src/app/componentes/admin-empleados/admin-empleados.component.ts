import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, computed, inject, signal } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

import { ConfirmDeleteDialogComponent } from '../confirm-delete-dialog/confirm-delete-dialog.component';
import { EmpleadoFormComponent } from '../empleado-form/empleado-form.component';
import { UiToastComponent } from '../ui-toast/ui-toast.component';
import { ConfirmacionEliminacion } from '../../modelos/confirmacion-eliminacion.model';
import { DepartamentoOption } from '../../modelos/departamento-option.model';
import { EmpleadoCrudPayload, EmpleadoGestion } from '../../modelos/empleado-gestion.model';
import { FormMode } from '../../modelos/estado-formulario-empleado.model';
import { AuthService } from '../../servicios/auth.service';
import { EmpleadoService } from '../../servicios/empleado.service';

@Component({
  selector: 'app-admin-empleados',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive, EmpleadoFormComponent, ConfirmDeleteDialogComponent, UiToastComponent],
  templateUrl: './admin-empleados.component.html',
  styleUrl: './admin-empleados.component.css'
})
export class AdminEmpleadosComponent {
  private readonly authService = inject(AuthService);
  private readonly empleadoService = inject(EmpleadoService);

  readonly nombre = () => this.authService.nombre();

  readonly empleados = signal<EmpleadoGestion[]>([]);
  readonly status = signal<'loading' | 'data' | 'empty' | 'error'>('loading');
  readonly errorMessage = signal('');

  readonly departamentos = signal<DepartamentoOption[]>([]);
  readonly loadingDepartamentos = signal(false);
  readonly formVisible = signal(false);
  readonly formMode = signal<FormMode>('create');
  readonly editingEmpleado = signal<EmpleadoGestion | null>(null);
  readonly submitting = signal(false);
  readonly formOperationError = signal<string | null>(null);

  readonly toastMessage = signal<string | null>(null);
  readonly toastVariant = signal<'success' | 'error' | 'warning'>('success');

  readonly confirmacion = signal<ConfirmacionEliminacion>({
    visible: false,
    clave: null,
    nombre: null
  });

  readonly currentPage = signal(0);
  readonly totalPages = signal(0);
  readonly totalElements = signal(0);

  readonly hasPrevious = computed(() => this.currentPage() > 0);
  readonly hasNext = computed(() => this.currentPage() + 1 < this.totalPages());
  readonly showNoDepartmentsWarning = computed(() => this.formVisible() && !this.loadingDepartamentos() && this.departamentos().length === 0);
  readonly departamentoNombrePorId = computed(() => {
    const byId = new Map<number, string>();
    this.departamentos().forEach((departamento) => {
      byId.set(departamento.id, departamento.nombre);
    });
    return byId;
  });

  private readonly pageSize = 10;
  private readonly defaultSort = 'nombre,asc';

  constructor() {
    this.loadPage(0);
    this.loadDepartamentos();
  }

  logout(): void {
    this.authService.logout();
  }

  retry(): void {
    this.loadPage(this.currentPage());
  }

  previousPage(): void {
    if (!this.hasPrevious()) {
      return;
    }

    this.loadPage(this.currentPage() - 1);
  }

  nextPage(): void {
    if (!this.hasNext()) {
      return;
    }

    this.loadPage(this.currentPage() + 1);
  }

  openCreateForm(): void {
    this.formMode.set('create');
    this.editingEmpleado.set(null);
    this.formOperationError.set(null);
    this.formVisible.set(true);
    this.loadDepartamentos();
  }

  openEditForm(empleado: EmpleadoGestion): void {
    this.formMode.set('edit');
    this.editingEmpleado.set(empleado);
    this.formOperationError.set(null);
    this.formVisible.set(true);
    this.loadDepartamentos();
  }

  closeForm(): void {
    this.formVisible.set(false);
    this.formOperationError.set(null);
  }

  submitForm(payload: EmpleadoCrudPayload): void {
    this.submitting.set(true);
    this.formOperationError.set(null);

    if (this.formMode() === 'create') {
      this.empleadoService.create(payload).subscribe({
        next: () => {
          this.showToast('Empleado creado exitosamente.', 'success');
          this.submitting.set(false);
          this.formVisible.set(false);
          this.loadPage(this.currentPage());
        },
        error: (error) => this.handleFormError(error)
      });
      return;
    }

    const clave = this.editingEmpleado()?.clave;
    if (!clave) {
      this.submitting.set(false);
      this.formOperationError.set('No fue posible identificar el empleado a editar.');
      return;
    }

    this.empleadoService.update(clave, payload).subscribe({
      next: () => {
        this.showToast('Empleado actualizado exitosamente.', 'success');
        this.submitting.set(false);
        this.formVisible.set(false);
        this.loadPage(this.currentPage());
      },
      error: (error) => this.handleFormError(error)
    });
  }

  requestDelete(empleado: EmpleadoGestion): void {
    if (!this.canDelete(empleado)) {
      return;
    }

    this.confirmacion.set({
      visible: true,
      clave: empleado.clave,
      nombre: empleado.nombre
    });
  }

  cancelDelete(): void {
    this.confirmacion.set({
      visible: false,
      clave: null,
      nombre: null
    });
  }

  confirmDelete(): void {
    const clave = this.confirmacion().clave;
    if (!clave) {
      this.cancelDelete();
      return;
    }

    this.empleadoService.delete(clave).subscribe({
      next: () => {
        this.cancelDelete();
        this.showToast('Empleado eliminado exitosamente.', 'success');
        this.loadPage(this.currentPage());
      },
      error: () => {
        this.cancelDelete();
        this.showToast('No se pudo eliminar el empleado.', 'error');
      }
    });
  }

  dismissToast(): void {
    this.toastMessage.set(null);
  }

  trackByClave(index: number, empleado: EmpleadoGestion): string {
    return empleado.clave || `${index}`;
  }

  getDepartamentoNombre(departamentoId: number | null): string {
    if (departamentoId == null) {
      return 'Sin asignar';
    }

    return this.departamentoNombrePorId().get(departamentoId) ?? 'Sin asignar';
  }

  canDelete(empleado: EmpleadoGestion | null | undefined): boolean {
    if (!empleado) {
      return false;
    }

    const empleadoEmail = empleado.email?.trim().toLowerCase();
    const sessionProvider = (this.authService as { session?: () => { email?: string } }).session;
    const emailProvider = (this.authService as { email?: () => string }).email;

    const sessionEmailRaw = typeof sessionProvider === 'function'
      ? sessionProvider()?.email
      : typeof emailProvider === 'function'
        ? emailProvider()
        : undefined;

    const sessionEmail = sessionEmailRaw?.trim().toLowerCase();

    if (!sessionEmail || !empleadoEmail) {
      return true;
    }

    return empleadoEmail !== sessionEmail;
  }

  private loadPage(page: number): void {
    this.status.set('loading');
    this.errorMessage.set('');

    this.empleadoService.list(page, this.pageSize, this.defaultSort).subscribe({
      next: (response) => {
        this.currentPage.set(response.number);
        this.totalPages.set(response.totalPages);
        this.totalElements.set(response.totalElements);

        if (!response.content.length) {
          this.empleados.set([]);
          this.status.set('empty');
          return;
        }

        this.empleados.set(response.content);
        this.status.set('data');

        if (!this.departamentos().length && !this.loadingDepartamentos()) {
          this.loadDepartamentos();
        }
      },
      error: () => {
        this.status.set('error');
        this.errorMessage.set('No pudimos cargar empleados. Intenta nuevamente.');
      }
    });
  }

  private loadDepartamentos(): void {
    if (this.loadingDepartamentos()) {
      return;
    }

    this.loadingDepartamentos.set(true);

    this.empleadoService.listDepartamentos(0, 100, 'nombre,asc').subscribe({
      next: (response) => {
        this.departamentos.set(response.content);
        this.loadingDepartamentos.set(false);
      },
      error: () => {
        this.departamentos.set([]);
        this.loadingDepartamentos.set(false);
        this.showToast('No fue posible cargar departamentos.', 'warning');
      }
    });
  }

  private handleFormError(error: unknown): void {
    this.submitting.set(false);

    if (error instanceof HttpErrorResponse && error.status === 409) {
      this.formOperationError.set('La clave de empleado ya existe.');
      return;
    }

    this.formOperationError.set('Ocurrió un error al guardar. Intenta nuevamente.');
  }

  private showToast(message: string, variant: 'success' | 'error' | 'warning'): void {
    this.toastMessage.set(message);
    this.toastVariant.set(variant);
  }
}

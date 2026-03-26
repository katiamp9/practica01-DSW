import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, computed, inject, signal } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

import { ConfirmDeleteDialogComponent } from '../confirm-delete-dialog/confirm-delete-dialog.component';
import { DepartamentoFormPayload, DepartamentoFormModalComponent } from '../departamento-form-modal/departamento-form-modal.component';
import { UiToastComponent } from '../ui-toast/ui-toast.component';
import { DepartamentoGestion } from '../../modelos/departamento-gestion.model';
import { DepartamentoFormMode } from '../../modelos/departamento-form-state.model';
import { AuthService } from '../../servicios/auth.service';
import { DepartamentoService } from '../../servicios/departamento.service';

const DELETE_CONFLICT_TOAST = 'No se puede eliminar: existen empleados asociados.';

@Component({
  selector: 'app-admin-departamentos',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive, DepartamentoFormModalComponent, ConfirmDeleteDialogComponent, UiToastComponent],
  templateUrl: './admin-departamentos.component.html',
  styleUrl: './admin-departamentos.component.css'
})
export class AdminDepartamentosComponent {
  private readonly authService = inject(AuthService);
  private readonly departamentoService = inject(DepartamentoService);

  readonly nombre = () => this.authService.nombre();

  readonly items = signal<DepartamentoGestion[]>([]);
  readonly status = signal<'loading' | 'data' | 'empty' | 'error'>('loading');
  readonly errorMessage = signal('');

  readonly searchTerm = signal('');
  readonly currentPage = signal(0);
  readonly totalPages = signal(0);
  readonly totalElements = signal(0);

  readonly formVisible = signal(false);
  readonly formMode = signal<DepartamentoFormMode>('create');
  readonly editingDepartamento = signal<DepartamentoGestion | null>(null);
  readonly submitting = signal(false);
  readonly formOperationError = signal<string | null>(null);

  readonly confirmacionDelete = signal<{ visible: boolean; id: number | null; nombre: string | null }>({
    visible: false,
    id: null,
    nombre: null
  });

  readonly toastMessage = signal<string | null>(null);
  readonly toastVariant = signal<'success' | 'error' | 'warning'>('success');

  readonly filteredItems = computed(() => {
    const term = this.searchTerm().trim().toLowerCase();
    if (!term) {
      return this.items();
    }

    return this.items().filter((item) => item.nombre.toLowerCase().includes(term));
  });

  readonly hasSearch = computed(() => this.searchTerm().trim().length > 0);
  readonly hasPrevious = computed(() => this.currentPage() > 0);
  readonly hasNext = computed(() => this.currentPage() + 1 < this.totalPages());

  private readonly pageSize = 10;
  private readonly defaultSort = 'nombre,asc';

  constructor() {
    const initialPage = this.readPageFromUrl();
    this.currentPage.set(initialPage);
    this.syncPageInUrl(initialPage);
    this.loadPage(initialPage);
  }

  logout(): void {
    this.authService.logout();
  }

  retry(): void {
    this.loadPage(this.currentPage());
  }

  setSearch(term: string): void {
    this.searchTerm.set(term);
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
    this.editingDepartamento.set(null);
    this.formOperationError.set(null);
    this.formVisible.set(true);
  }

  openEditForm(departamento: DepartamentoGestion): void {
    this.formMode.set('edit');
    this.editingDepartamento.set(departamento);
    this.formOperationError.set(null);
    this.formVisible.set(true);
  }

  handleModalCancel(dirty: boolean): void {
    if (dirty && !window.confirm('Tienes cambios sin guardar. ¿Deseas descartarlos?')) {
      return;
    }

    this.formVisible.set(false);
    this.formOperationError.set(null);
  }

  submitForm(payload: DepartamentoFormPayload): void {
    this.submitting.set(true);
    this.formOperationError.set(null);

    if (this.formMode() === 'create') {
      this.departamentoService.create(payload).subscribe({
        next: () => {
          this.showToast('Departamento creado exitosamente.', 'success');
          this.submitting.set(false);
          this.formVisible.set(false);
          this.currentPage.set(0);
          this.totalPages.set(0);
          this.totalElements.set(0);
          this.loadPage(0);
        },
        error: (error) => this.handleFormError(error)
      });
      return;
    }

    const id = this.editingDepartamento()?.id;
    if (id == null) {
      this.submitting.set(false);
      this.formOperationError.set('No fue posible identificar el departamento a editar.');
      return;
    }

    this.departamentoService.update(id, payload).subscribe({
      next: () => {
        this.showToast('Departamento actualizado exitosamente.', 'success');
        this.submitting.set(false);
        this.formVisible.set(false);
        this.loadPage(this.currentPage());
      },
      error: (error) => this.handleFormError(error)
    });
  }

  deleteDepartamento(departamento: DepartamentoGestion): void {
    if (departamento.totalEmpleados > 0) {
      return;
    }

    this.confirmacionDelete.set({
      visible: true,
      id: departamento.id,
      nombre: departamento.nombre
    });
  }

  cancelDeleteConfirm(): void {
    this.confirmacionDelete.set({
      visible: false,
      id: null,
      nombre: null
    });
  }

  confirmDelete(): void {
    const departamentoId = this.confirmacionDelete().id;
    if (departamentoId == null) {
      this.cancelDeleteConfirm();
      return;
    }

    this.departamentoService.delete(departamentoId).subscribe({
      next: () => {
        this.cancelDeleteConfirm();
        this.showToast('Departamento eliminado exitosamente.', 'success');
        this.loadPage(this.currentPage());
      },
      error: (error) => {
        this.cancelDeleteConfirm();
        if (error instanceof HttpErrorResponse && error.status === 409) {
          this.showToast(DELETE_CONFLICT_TOAST, 'error');
          return;
        }

        this.showToast('No se pudo eliminar el departamento.', 'error');
      }
    });
  }

  deleteTooltip(departamento: DepartamentoGestion): string {
    return departamento.totalEmpleados > 0
      ? 'No se puede eliminar: tiene empleados asociados'
      : 'Eliminar departamento';
  }

  dismissToast(): void {
    this.toastMessage.set(null);
  }

  trackById(index: number, item: DepartamentoGestion): number {
    return item.id ?? index;
  }

  private loadPage(page: number | undefined): void {
    const safePage = this.normalizePage(page);

    this.status.set('loading');
    this.errorMessage.set('');
    this.items.set([]);
    this.syncPageInUrl(safePage);

    this.departamentoService.list(safePage, this.pageSize, this.defaultSort).subscribe({
      next: (response) => {
        const pageFromResponse = (response as { number?: number | null; page?: { number?: number | null } }).number
          ?? (response as { page?: { number?: number | null } }).page?.number
          ?? safePage;
        const totalPagesFromResponse = (response as { totalPages?: number | null; page?: { totalPages?: number | null } }).totalPages
          ?? (response as { page?: { totalPages?: number | null } }).page?.totalPages
          ?? 0;
        const totalElementsFromResponse = (response as { totalElements?: number | null; page?: { totalElements?: number | null } }).totalElements
          ?? (response as { page?: { totalElements?: number | null } }).page?.totalElements
          ?? 0;

        const resolvedPage = this.normalizePage(pageFromResponse);
        const resolvedTotalPages = Math.max(0, Math.floor(totalPagesFromResponse));
        const resolvedTotalElements = Math.max(0, Math.floor(totalElementsFromResponse));

        this.currentPage.set(resolvedPage);
        this.totalPages.set(resolvedTotalPages);
        this.totalElements.set(resolvedTotalElements);
        this.syncPageInUrl(resolvedPage);

        if (!response.content.length) {
          this.items.set([]);
          this.status.set('empty');
          return;
        }

        this.items.set(response.content);
        this.status.set('data');
      },
      error: () => {
        this.status.set('error');
        this.errorMessage.set('No pudimos cargar departamentos. Intenta nuevamente.');
      }
    });
  }

  private normalizePage(page: number | undefined): number {
    if (typeof page !== 'number' || Number.isNaN(page) || page < 0) {
      return 0;
    }

    return Math.floor(page);
  }

  private readPageFromUrl(): number {
    const params = new URLSearchParams(window.location.search);
    const rawPage = params.get('page');
    if (!rawPage) {
      return 0;
    }

    return this.normalizePage(Number(rawPage));
  }

  private syncPageInUrl(page: number): void {
    const url = new URL(window.location.href);
    url.searchParams.set('page', String(page));
    window.history.replaceState({}, '', `${url.pathname}${url.search}${url.hash}`);
  }

  private handleFormError(error: unknown): void {
    this.submitting.set(false);

    if (error instanceof HttpErrorResponse && error.status === 400) {
      const message = error.error?.message || error.error?.detail || 'Datos inválidos para guardar departamento.';
      this.formOperationError.set(message);
      return;
    }

    this.formOperationError.set('Ocurrió un error al guardar. Intenta nuevamente.');
  }

  private showToast(message: string, variant: 'success' | 'error' | 'warning'): void {
    this.toastMessage.set(message);
    this.toastVariant.set(variant);
  }
}

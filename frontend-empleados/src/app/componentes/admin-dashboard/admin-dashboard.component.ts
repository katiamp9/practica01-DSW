import { CommonModule } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

import { EmpleadoListado } from '../../modelos/empleado-listado.model';
import { roleBadgeClass } from '../../modelos/app-roles';
import { EmpleadoQueryService } from '../../servicios/empleado-query.service';
import { AuthService } from '../../servicios/auth.service';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.css'
})
export class AdminDashboardComponent {
  private readonly authService = inject(AuthService);
  private readonly empleadoQueryService = inject(EmpleadoQueryService);

  readonly nombre = () => this.authService.nombre();

  readonly empleados = signal<EmpleadoListado[]>([]);
  readonly status = signal<'loading' | 'data' | 'empty' | 'error'>('loading');
  readonly emptyMessage = signal('');
  readonly errorMessage = signal('');
  readonly searchTerm = signal('');
  readonly currentPage = signal(0);
  readonly totalPages = signal(0);
  readonly totalElements = signal(0);

  readonly filteredEmpleados = computed(() => {
    const term = this.searchTerm().trim().toLowerCase();
    if (!term) {
      return this.empleados();
    }

    return this.empleados().filter((empleado) => {
      const nombre = empleado.nombre?.toLowerCase() ?? '';
      const clave = empleado.clave?.toLowerCase() ?? '';
      return nombre.includes(term) || clave.includes(term);
    });
  });

  readonly hasSearch = computed(() => this.searchTerm().trim().length > 0);

  readonly hasPrevious = computed(() => this.currentPage() > 0);
  readonly hasNext = computed(() => this.currentPage() + 1 < this.totalPages());

  private readonly pageSize = 5;
  private readonly defaultSort = 'clave,asc';

  constructor() {
    this.loadPage(0);
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

  nextPage(): void {
    if (!this.hasNext()) {
      return;
    }

    this.loadPage(this.currentPage() + 1);
  }

  previousPage(): void {
    if (!this.hasPrevious()) {
      return;
    }

    this.loadPage(this.currentPage() - 1);
  }

  roleClass(role: string): string {
    return roleBadgeClass(role);
  }

  trackByClave(index: number, empleado: EmpleadoListado): string {
    return empleado.clave || `${index}`;
  }

  private loadPage(page: number): void {
    this.status.set('loading');
    this.errorMessage.set('');
    this.emptyMessage.set('');

    this.empleadoQueryService.list(page, this.pageSize, this.defaultSort).subscribe({
      next: (response) => {
        const pageNumber = response.number ?? (response as { page?: { number?: number | null } }).page?.number ?? 0;
        const pagesTotal = response.totalPages ?? (response as { page?: { totalPages?: number | null } }).page?.totalPages ?? 0;
        const elementsTotal = response.totalElements ?? (response as { page?: { totalElements?: number | null } }).page?.totalElements ?? 0;

        this.currentPage.set(Number.isFinite(pageNumber) && pageNumber >= 0 ? Math.floor(pageNumber) : 0);
        this.totalPages.set(Number.isFinite(pagesTotal) && pagesTotal >= 0 ? Math.floor(pagesTotal) : 0);
        this.totalElements.set(Number.isFinite(elementsTotal) && elementsTotal >= 0 ? Math.floor(elementsTotal) : 0);

        if (!response.content.length) {
          this.empleados.set([]);
          this.status.set('empty');
          this.emptyMessage.set('No hay empleados para mostrar por ahora.');
          return;
        }

        this.empleados.set(response.content);
        this.status.set('data');
      },
      error: () => {
        this.status.set('error');
        this.errorMessage.set('No pudimos cargar empleados. Intenta nuevamente.');
      }
    });
  }
}
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
  readonly currentPage = signal(0);
  readonly totalPages = signal(0);
  readonly totalElements = signal(0);

  readonly hasPrevious = computed(() => this.currentPage() > 0);
  readonly hasNext = computed(() => this.currentPage() + 1 < this.totalPages());

  private readonly pageSize = 10;
  private readonly defaultSort = 'nombre,asc';

  constructor() {
    this.loadPage(0);
  }

  logout(): void {
    this.authService.logout();
  }

  retry(): void {
    this.loadPage(this.currentPage());
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
        this.currentPage.set(response.number);
        this.totalPages.set(response.totalPages);
        this.totalElements.set(response.totalElements);

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
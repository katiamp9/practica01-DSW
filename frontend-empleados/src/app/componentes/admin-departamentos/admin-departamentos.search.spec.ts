import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { ActivatedRoute } from '@angular/router';

import { AuthService } from '../../servicios/auth.service';
import { DepartamentoService } from '../../servicios/departamento.service';
import { AdminDepartamentosComponent } from './admin-departamentos.component';

describe('AdminDepartamentosComponent search', () => {
  let component: AdminDepartamentosComponent;
  let fixture: ComponentFixture<AdminDepartamentosComponent>;

  beforeEach(async () => {
    const departamentoService = jasmine.createSpyObj<DepartamentoService>('DepartamentoService', [
      'list',
      'create',
      'update',
      'delete'
    ]);

    departamentoService.list.and.returnValue(
      of({
        content: [
          { id: 1, nombre: 'Sistemas', totalEmpleados: 2 },
          { id: 2, nombre: 'Finanzas', totalEmpleados: 0 }
        ],
        number: 0,
        size: 10,
        totalElements: 2,
        totalPages: 1,
        first: true,
        last: true
      })
    );

    const authService = jasmine.createSpyObj<AuthService>('AuthService', ['nombre', 'logout']);
    authService.nombre.and.returnValue('Admin');

    await TestBed.configureTestingModule({
      imports: [AdminDepartamentosComponent],
      providers: [
        { provide: DepartamentoService, useValue: departamentoService },
        { provide: AuthService, useValue: authService },
        { provide: ActivatedRoute, useValue: { snapshot: { queryParamMap: new Map() } } }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AdminDepartamentosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should derive filteredItems with computed signal without extra requests', () => {
    component.setSearch('fin');

    expect(component.filteredItems().length).toBe(1);
    expect(component.filteredItems()[0].nombre).toBe('Finanzas');
  });
});

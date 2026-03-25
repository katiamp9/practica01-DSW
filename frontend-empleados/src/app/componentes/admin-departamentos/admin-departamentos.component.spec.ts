import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { of } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from '../../servicios/auth.service';
import { DepartamentoService } from '../../servicios/departamento.service';
import { PageResponse } from '../../modelos/page-response.model';
import { AdminDepartamentosComponent } from './admin-departamentos.component';

const mockPage: PageResponse<any> = {
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
};

describe('AdminDepartamentosComponent', () => {
  let component: AdminDepartamentosComponent;
  let fixture: ComponentFixture<AdminDepartamentosComponent>;
  let departamentoService: jasmine.SpyObj<DepartamentoService>;
  let authService: jasmine.SpyObj<AuthService>;

  beforeEach(async () => {
    departamentoService = jasmine.createSpyObj<DepartamentoService>('DepartamentoService', [
      'list',
      'create',
      'update',
      'delete'
    ]);
    departamentoService.list.and.returnValue(of(mockPage));
    departamentoService.create.and.returnValue(of({ id: 3, nombre: 'Legal', totalEmpleados: 0 }));
    departamentoService.update.and.returnValue(of({ id: 1, nombre: 'Sistemas', totalEmpleados: 2 }));
    departamentoService.delete.and.returnValue(of(void 0));

    authService = jasmine.createSpyObj<AuthService>('AuthService', ['nombre', 'logout']);
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

  it('should load initial data and set data status', () => {
    expect(departamentoService.list).toHaveBeenCalled();
    expect(component.status()).toBe('data');
    expect(component.totalElements()).toBe(2);
  });

  it('should filter rows with computed search term', () => {
    component.setSearch('sis');
    const filtered = component.filteredItems();

    expect(filtered.length).toBe(1);
    expect(filtered[0].nombre).toBe('Sistemas');
  });

  it('should show toast after creating departamento', fakeAsync(() => {
    component.openCreateForm();
    component.submitForm({ nombre: 'Legal' });
    tick();

    expect(departamentoService.create).toHaveBeenCalledWith({ nombre: 'Legal' });
    expect(component.toastMessage()).toContain('creado');
  }));
});

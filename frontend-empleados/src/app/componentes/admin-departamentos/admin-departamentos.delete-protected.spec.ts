import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { throwError, of } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from '../../servicios/auth.service';
import { DepartamentoService } from '../../servicios/departamento.service';
import { PageResponse } from '../../modelos/page-response.model';
import { AdminDepartamentosComponent } from './admin-departamentos.component';

const page: PageResponse<any> = {
  content: [{ id: 1, nombre: 'Sistemas', totalEmpleados: 0 }],
  number: 0,
  size: 10,
  totalElements: 1,
  totalPages: 1,
  first: true,
  last: true
};

describe('AdminDepartamentosComponent protected delete', () => {
  let component: AdminDepartamentosComponent;
  let fixture: ComponentFixture<AdminDepartamentosComponent>;
  let departamentoService: jasmine.SpyObj<DepartamentoService>;

  beforeEach(async () => {
    departamentoService = jasmine.createSpyObj<DepartamentoService>('DepartamentoService', [
      'list',
      'create',
      'update',
      'delete'
    ]);
    departamentoService.list.and.returnValue(of(page));

    await TestBed.configureTestingModule({
      imports: [AdminDepartamentosComponent],
      providers: [
        { provide: DepartamentoService, useValue: departamentoService },
        { provide: AuthService, useValue: jasmine.createSpyObj<AuthService>('AuthService', ['nombre', 'logout']) },
        { provide: ActivatedRoute, useValue: { snapshot: { queryParamMap: new Map() } } }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AdminDepartamentosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should show canonical conflict toast when backend returns 409', fakeAsync(() => {
    departamentoService.delete.and.returnValue(
      throwError(() => new HttpErrorResponse({ status: 409, statusText: 'Conflict' }))
    );

    component.deleteDepartamento({ id: 1, nombre: 'Sistemas', totalEmpleados: 0 });
    component.confirmDelete();
    tick();

    expect(component.toastMessage()).toBe('No se puede eliminar: existen empleados asociados.');
    expect(component.toastVariant()).toBe('error');
  }));

  it('should disable delete button for departamentos with employees', () => {
    const tooltip = component.deleteTooltip({ id: 10, nombre: 'RRHH', totalEmpleados: 3 });

    expect(tooltip).toContain('empleados asociados');
  });
});

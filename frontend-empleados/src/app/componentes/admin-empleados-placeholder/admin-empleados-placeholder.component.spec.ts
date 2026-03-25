/// <reference types="jasmine" />

import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { AdminEmpleadosPlaceholderComponent } from './admin-empleados-placeholder.component';

describe('AdminEmpleadosPlaceholderComponent', () => {
  it('should render Proximamente placeholder message', () => {
    TestBed.configureTestingModule({
      imports: [AdminEmpleadosPlaceholderComponent],
      providers: [provideRouter([])]
    });

    const fixture = TestBed.createComponent(AdminEmpleadosPlaceholderComponent);
    fixture.detectChanges();

    expect(fixture.nativeElement.textContent).toContain('Próximamente');
  });
});

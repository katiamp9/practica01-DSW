/// <reference types="jasmine" />

import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';

import { AdminDepartamentosPlaceholderComponent } from './admin-departamentos-placeholder.component';

describe('AdminDepartamentosPlaceholderComponent', () => {
  it('should render Proximamente placeholder message', () => {
    TestBed.configureTestingModule({
      imports: [AdminDepartamentosPlaceholderComponent],
      providers: [provideRouter([])]
    });

    const fixture = TestBed.createComponent(AdminDepartamentosPlaceholderComponent);
    fixture.detectChanges();

    expect(fixture.nativeElement.textContent).toContain('Próximamente');
  });
});

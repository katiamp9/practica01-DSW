import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DepartamentoFormModalComponent } from './departamento-form-modal.component';

describe('DepartamentoFormModalComponent', () => {
  let component: DepartamentoFormModalComponent;
  let fixture: ComponentFixture<DepartamentoFormModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DepartamentoFormModalComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(DepartamentoFormModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should emit submitted when form is valid', () => {
    const submittedSpy = jasmine.createSpy('submitted');
    component.submitted.subscribe(submittedSpy);

    component.form.controls.nombre.setValue('Finanzas');
    component.onSubmit();

    expect(submittedSpy).toHaveBeenCalledWith({ nombre: 'Finanzas' });
  });

  it('should emit canceled with dirty true when form has changes', () => {
    const canceledSpy = jasmine.createSpy('canceled');
    component.canceled.subscribe(canceledSpy);

    component.form.controls.nombre.setValue('Operaciones');
    component.form.markAsDirty();
    component.onCancel();

    expect(canceledSpy).toHaveBeenCalledWith(true);
  });
});

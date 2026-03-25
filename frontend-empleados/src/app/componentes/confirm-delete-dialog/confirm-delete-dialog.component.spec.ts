/// <reference types="jasmine" />

import { TestBed } from '@angular/core/testing';

import { ConfirmDeleteDialogComponent } from './confirm-delete-dialog.component';

describe('ConfirmDeleteDialogComponent', () => {
  it('should emit confirmed and canceled events', () => {
    TestBed.configureTestingModule({
      imports: [ConfirmDeleteDialogComponent]
    });

    const fixture = TestBed.createComponent(ConfirmDeleteDialogComponent);
    const component = fixture.componentInstance;
    component.visible = true;
    component.empleadoNombre = 'Ana';

    const confirmedSpy = jasmine.createSpy('confirmed');
    const canceledSpy = jasmine.createSpy('canceled');
    component.confirmed.subscribe(confirmedSpy);
    component.canceled.subscribe(canceledSpy);

    fixture.detectChanges();

    const buttons = fixture.nativeElement.querySelectorAll('button');
    buttons[0].click();
    buttons[1].click();

    expect(canceledSpy).toHaveBeenCalled();
    expect(confirmedSpy).toHaveBeenCalled();
  });
});

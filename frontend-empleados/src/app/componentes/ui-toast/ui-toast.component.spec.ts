/// <reference types="jasmine" />

import { TestBed } from '@angular/core/testing';

import { UiToastComponent } from './ui-toast.component';

describe('UiToastComponent', () => {
  it('should render message and emit dismiss event', () => {
    TestBed.configureTestingModule({
      imports: [UiToastComponent]
    });

    const fixture = TestBed.createComponent(UiToastComponent);
    const component = fixture.componentInstance;
    component.message = 'Guardado';
    component.variant = 'success';

    const dismissedSpy = jasmine.createSpy('dismissed');
    component.dismissed.subscribe(dismissedSpy);

    fixture.detectChanges();

    expect(fixture.nativeElement.textContent).toContain('Guardado');

    const button: HTMLButtonElement = fixture.nativeElement.querySelector('button');
    button.click();

    expect(dismissedSpy).toHaveBeenCalled();
  });
});

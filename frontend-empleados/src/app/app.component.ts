import { Component, signal } from '@angular/core';

@Component({
  selector: 'app-root',
  standalone: true,
  template: `
    <main>
      <h1>Empleados Frontend</h1>
      <p>Base Angular 21 lista para integración con backend.</p>
      @if (ready()) {
        <small>Estado: listo</small>
      }
    </main>
  `,
  styles: [
    `
      main {
        padding: 2rem;
      }
    `
  ]
})
export class AppComponent {
  protected readonly ready = signal(true);
}

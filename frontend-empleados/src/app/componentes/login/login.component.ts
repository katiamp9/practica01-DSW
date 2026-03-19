import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { APP_ROLES } from '../../modelos/app-roles';
import { AuthApiService } from '../../servicios/auth-api.service';
import { AuthService } from '../../servicios/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  private readonly formBuilder = inject(FormBuilder);
  private readonly authApiService = inject(AuthApiService);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  readonly loading = signal(false);
  readonly errorMessage = signal('');

  readonly loginForm = this.formBuilder.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(4)]]
  });

  submit(): void {
    if (this.loginForm.invalid || this.loading()) {
      this.loginForm.markAllAsTouched();
      return;
    }

    const email = this.loginForm.controls.email.value?.trim() ?? '';
    const password = this.loginForm.controls.password.value ?? '';

    this.loading.set(true);
    this.errorMessage.set('');

    this.authApiService.login({ email, password }).subscribe({
      next: (response) => {
        const success = this.authService.loginSuccess(email, password, response);
        if (!success) {
          this.errorMessage.set('No tienes permisos para acceder.');
          this.loading.set(false);
          return;
        }

        if (response.rol === APP_ROLES.ADMIN) {
          this.router.navigate(['/admin-dashboard']);
        } else if (response.rol === APP_ROLES.USER) {
          this.router.navigate(['/home']);
        } else {
          this.authService.clearSessionAndRedirect();
          this.errorMessage.set('Rol no permitido.');
        }
        this.loading.set(false);
      },
      error: () => {
        this.errorMessage.set('Credenciales inválidas. Intenta nuevamente.');
        this.loading.set(false);
      }
    });
  }
}
import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-admin-empleados-placeholder',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './admin-empleados-placeholder.component.html',
  styleUrl: './admin-empleados-placeholder.component.css'
})
export class AdminEmpleadosPlaceholderComponent {}

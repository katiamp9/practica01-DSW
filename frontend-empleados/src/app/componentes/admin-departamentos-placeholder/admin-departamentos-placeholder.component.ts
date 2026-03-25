import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-admin-departamentos-placeholder',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './admin-departamentos-placeholder.component.html',
  styleUrl: './admin-departamentos-placeholder.component.css'
})
export class AdminDepartamentosPlaceholderComponent {}

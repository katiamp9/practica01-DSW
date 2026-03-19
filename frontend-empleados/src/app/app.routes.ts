import { Routes } from '@angular/router';

import { AdminDashboardComponent } from './componentes/admin-dashboard/admin-dashboard.component';
import { HomeComponent } from './componentes/home/home.component';
import { LoginComponent } from './componentes/login/login.component';
import { authGuard } from './guards/auth.guard';
import { roleGuard } from './guards/role.guard';
import { APP_ROLES } from './modelos/app-roles';

export const appRoutes: Routes = [
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'admin-dashboard',
    component: AdminDashboardComponent,
    canActivate: [authGuard, roleGuard],
    data: { role: APP_ROLES.ADMIN }
  },
  {
    path: 'home',
    component: HomeComponent,
    canActivate: [authGuard, roleGuard],
    data: { role: APP_ROLES.USER }
  },
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'login'
  },
  {
    path: '**',
    redirectTo: 'login'
  }
];
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';
import { DepartamentoOption } from '../modelos/departamento-option.model';
import { EmpleadoCrudPayload, EmpleadoGestion } from '../modelos/empleado-gestion.model';
import { PageResponse } from '../modelos/page-response.model';

@Injectable({
  providedIn: 'root'
})
export class EmpleadoService {
  private readonly httpClient = inject(HttpClient);
  private readonly empleadosEndpoint = `${environment.apiBasePath}/empleados`;
  private readonly departamentosEndpoint = `${environment.apiBasePath}/departamentos`;

  list(page: number, size = 10, sort = 'clave,asc'): Observable<PageResponse<EmpleadoGestion>> {
    const enforcedSort = 'clave,asc';

    return this.httpClient.get<PageResponse<EmpleadoGestion>>(this.empleadosEndpoint, {
      params: this.pageParams(page, size, enforcedSort)
    });
  }

  listDepartamentos(page = 0, size = 100, sort = 'nombre,asc'): Observable<PageResponse<DepartamentoOption>> {
    return this.httpClient.get<PageResponse<DepartamentoOption>>(this.departamentosEndpoint, {
      params: this.pageParams(page, size, sort)
    });
  }

  create(payload: EmpleadoCrudPayload): Observable<EmpleadoGestion> {
    return this.httpClient.post<EmpleadoGestion>(this.empleadosEndpoint, payload);
  }

  update(clave: string, payload: EmpleadoCrudPayload): Observable<EmpleadoGestion> {
    return this.httpClient.put<EmpleadoGestion>(`${this.empleadosEndpoint}/${clave}`, payload);
  }

  delete(clave: string): Observable<void> {
    return this.httpClient.delete<void>(`${this.empleadosEndpoint}/${clave}`);
  }

  private pageParams(page: number, size: number, sort: string): HttpParams {
    const safePage = Number.isFinite(page) && page >= 0 ? Math.floor(page) : 0;

    return new HttpParams()
      .set('page', String(safePage))
      .set('size', String(size))
      .set('sort', sort);
  }
}

import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';
import { DepartamentoCrudPayload, DepartamentoGestion } from '../modelos/departamento-gestion.model';
import { PageResponse } from '../modelos/page-response.model';

@Injectable({
  providedIn: 'root'
})
export class DepartamentoService {
  private readonly httpClient = inject(HttpClient);
  private readonly departamentosEndpoint = `${environment.apiBasePath}/departamentos`;

  list(page: number, size = 5, sort = 'nombre,asc'): Observable<PageResponse<DepartamentoGestion>> {
    return this.httpClient.get<PageResponse<DepartamentoGestion>>(this.departamentosEndpoint, {
      params: this.pageParams(page, size, sort)
    });
  }

  create(payload: DepartamentoCrudPayload): Observable<DepartamentoGestion> {
    return this.httpClient.post<DepartamentoGestion>(this.departamentosEndpoint, payload);
  }

  update(id: number, payload: DepartamentoCrudPayload): Observable<DepartamentoGestion> {
    return this.httpClient.put<DepartamentoGestion>(`${this.departamentosEndpoint}/${id}`, payload);
  }

  delete(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.departamentosEndpoint}/${id}`);
  }

  private pageParams(page: number, size: number, sort: string): HttpParams {
    const safePage = Number.isFinite(page) && page >= 0 ? Math.floor(page) : 0;

    return new HttpParams()
      .set('page', String(safePage))
      .set('size', String(size))
      .set('sort', sort);
  }
}

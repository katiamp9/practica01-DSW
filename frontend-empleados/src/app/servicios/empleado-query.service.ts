import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';
import { EmpleadoListado } from '../modelos/empleado-listado.model';
import { PageResponse } from '../modelos/page-response.model';

@Injectable({
  providedIn: 'root'
})
export class EmpleadoQueryService {
  private readonly httpClient = inject(HttpClient);
  private readonly endpointUrl = `${environment.apiBasePath}/empleados`;

  list(page: number, size = 10, sort = 'nombre,asc'): Observable<PageResponse<EmpleadoListado>> {
    const params = this.buildParams(page, size, sort);
    return this.httpClient.get<PageResponse<EmpleadoListado>>(this.endpointUrl, { params });
  }

  private buildParams(page: number, size: number, sort: string): HttpParams {
    return new HttpParams()
      .set('page', String(page))
      .set('size', String(size))
      .set('sort', sort);
  }
}

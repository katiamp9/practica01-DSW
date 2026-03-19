import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ApiClientService {
  private readonly httpClient = inject(HttpClient);
  private readonly apiBasePath = environment.apiBasePath;

  get<T>(path: string): Observable<T> {
    return this.httpClient.get<T>(`${this.apiBasePath}${path}`);
  }

  post<T>(path: string, body: unknown): Observable<T> {
    return this.httpClient.post<T>(`${this.apiBasePath}${path}`, body);
  }

  put<T>(path: string, body: unknown): Observable<T> {
    return this.httpClient.put<T>(`${this.apiBasePath}${path}`, body);
  }

  delete<T>(path: string): Observable<T> {
    return this.httpClient.delete<T>(`${this.apiBasePath}${path}`);
  }
}

import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class BackendHealthService {
  private readonly httpClient = inject(HttpClient);

  checkHealth(): Observable<unknown> {
    return this.httpClient.get('/api/v1/auth/login');
  }
}

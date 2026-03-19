import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';
import { AuthLoginRequest, AuthLoginResponse } from '../modelos/auth-login.model';

@Injectable({
  providedIn: 'root'
})
export class AuthApiService {
  private readonly httpClient = inject(HttpClient);
  private readonly loginUrl = `${environment.apiBasePath}/auth/login`;

  login(payload: AuthLoginRequest): Observable<AuthLoginResponse> {
    return this.httpClient.post<AuthLoginResponse>(this.loginUrl, payload);
  }
}
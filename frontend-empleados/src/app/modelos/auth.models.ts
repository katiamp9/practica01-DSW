export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  rol: string;
  nombre: string;
}

export interface SessionState {
  autenticado: boolean;
  email: string;
  nombre: string;
  rol: string;
}
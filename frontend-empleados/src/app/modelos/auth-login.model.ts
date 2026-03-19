export interface AuthLoginRequest {
  email: string;
  password: string;
}

export interface AuthLoginResponse {
  rol: string;
  nombre: string;
}
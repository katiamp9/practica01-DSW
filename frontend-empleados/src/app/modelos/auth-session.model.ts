export interface AuthSession {
  isAuthenticated: boolean;
  basicAuthToken: string;
  role: string;
  email: string;
  nombre: string;
}
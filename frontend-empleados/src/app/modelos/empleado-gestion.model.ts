export interface EmpleadoGestion {
  clave: string;
  nombre: string;
  email: string | null;
  direccion?: string;
  telefono?: string;
  departamentoId: number | null;
  rol?: string;
  password?: string;
}

export interface EmpleadoCrudPayload {
  nombre: string;
  email: string;
  direccion?: string;
  telefono?: string;
  departamentoId: number;
  password?: string;
}

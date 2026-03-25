export interface EmpleadoListado {
  clave: string;
  nombre: string;
  direccion?: string;
  telefono?: string;
  departamentoId?: number | null;
  rol: string;
}

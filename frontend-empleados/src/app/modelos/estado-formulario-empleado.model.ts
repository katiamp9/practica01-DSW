export type FormMode = 'create' | 'edit';

export interface EstadoFormularioEmpleado {
  mode: FormMode;
  submitting: boolean;
  loadingDepartamentos: boolean;
  operationError: string | null;
}

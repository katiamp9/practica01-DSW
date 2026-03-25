import { DepartamentoCrudPayload } from './departamento-gestion.model';

export type DepartamentoFormMode = 'create' | 'edit';

export interface DepartamentoFormState {
  visible: boolean;
  mode: DepartamentoFormMode;
  submitting: boolean;
  dirty: boolean;
  model: DepartamentoCrudPayload;
  errorMessage: string | null;
}

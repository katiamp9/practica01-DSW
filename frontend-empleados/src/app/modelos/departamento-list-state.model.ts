import { DepartamentoGestion } from './departamento-gestion.model';

export type DepartamentoListStatus = 'loading' | 'data' | 'empty' | 'error';

export interface DepartamentoListState {
  items: DepartamentoGestion[];
  status: DepartamentoListStatus;
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  sort: string;
  searchTerm: string;
  errorMessage: string;
}

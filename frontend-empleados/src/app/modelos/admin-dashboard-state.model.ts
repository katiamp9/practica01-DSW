export type AdminDashboardStatus = 'loading' | 'data' | 'empty' | 'error';

export interface AdminDashboardState {
  status: AdminDashboardStatus;
  message: string;
}

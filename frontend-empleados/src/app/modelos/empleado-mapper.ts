import { EmpleadoCrudPayload, EmpleadoGestion } from './empleado-gestion.model';

export function toEmpleadoCrudPayload(model: EmpleadoGestion): EmpleadoCrudPayload {
  const trimmedPassword = model.password?.trim();

  const payload: EmpleadoCrudPayload = {
    nombre: model.nombre?.trim() || '',
    email: model.email?.trim() || '',
    direccion: model.direccion?.trim() || undefined,
    telefono: model.telefono?.trim() || undefined,
    departamentoId: model.departamentoId ?? 0
  };

  if (trimmedPassword) {
    payload.password = trimmedPassword;
  }

  return payload;
}

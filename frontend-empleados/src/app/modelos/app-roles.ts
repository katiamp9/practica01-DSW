export const APP_ROLES = {
  ADMIN: 'ROLE_ADMIN',
  USER: 'ROLE_USER'
} as const;

export type AppRole = (typeof APP_ROLES)[keyof typeof APP_ROLES];

export const ALL_ROLES: readonly AppRole[] = Object.values(APP_ROLES);

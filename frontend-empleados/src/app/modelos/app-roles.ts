export const APP_ROLES = {
  ADMIN: 'ROLE_ADMIN',
  USER: 'ROLE_USER'
} as const;

export type AppRole = (typeof APP_ROLES)[keyof typeof APP_ROLES];

export const ALL_ROLES: readonly AppRole[] = Object.values(APP_ROLES);

export const ROLE_BADGE_CLASS: Record<AppRole, string> = {
  [APP_ROLES.ADMIN]: 'badge-admin',
  [APP_ROLES.USER]: 'badge-user'
};

export function normalizeAppRole(role: string): AppRole | null {
  if (!role) {
    return null;
  }

  const normalized = role.trim().toUpperCase();
  if (ALL_ROLES.includes(normalized as AppRole)) {
    return normalized as AppRole;
  }

  if (normalized === 'ADMIN') {
    return APP_ROLES.ADMIN;
  }

  if (normalized === 'USER') {
    return APP_ROLES.USER;
  }

  return null;
}

export function roleBadgeClass(role: string): string {
  const canonicalRole = normalizeAppRole(role);
  if (!canonicalRole) {
    return 'badge-unknown';
  }

  return ROLE_BADGE_CLASS[canonicalRole];
}

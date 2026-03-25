const CORPORATE_EMAIL_PATTERN = /^[a-zA-Z0-9._%+-]+@empresa\.com$/;

export function isCorporateEmail(value: string): boolean {
  return CORPORATE_EMAIL_PATTERN.test(value.trim());
}

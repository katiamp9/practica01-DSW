describe('Departamentos auth', () => {
  it('redirige a login al intentar entrar a ruta protegida sin sesión', () => {
    cy.visit('/admin/departamentos');
    cy.url().should('include', '/login');
  });
});

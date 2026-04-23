describe('Login E2E', () => {
  it('debe autenticar y redirigir al dashboard', () => {
    cy.intercept('POST', '**/api/v1/auth/login', {
      statusCode: 200,
      body: {
        rol: 'ROLE_ADMIN',
        nombre: 'Administrador'
      }
    }).as('loginRequest');

    cy.visit('/login');

    cy.get('#email').should('be.visible').clear().type('admin@empresa.com');
    cy.get('#password').should('be.visible').clear().type('admin123');

    cy.contains('button[type="submit"]', /Ingresar|Iniciar Sesión/i).click();

    cy.wait('@loginRequest');
    cy.url().should('include', '/admin-dashboard');
  });
});

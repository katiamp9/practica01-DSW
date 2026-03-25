describe('Login base', () => {
  it('renderiza login y mantiene palette de modo oscuro GitHub', () => {
    cy.visit('/login');
    cy.contains('Iniciar sesión').should('be.visible');
    cy.get('body').should('have.css', 'background-color', 'rgb(13, 17, 23)');
  });
});

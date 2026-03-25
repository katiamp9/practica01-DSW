describe('Departamentos dark mode', () => {
  it('carga la aplicación con fondo GitHub Dark #0d1117', () => {
    cy.visit('/login');
    cy.get('body').should('have.css', 'background-color', 'rgb(13, 17, 23)');
  });
});

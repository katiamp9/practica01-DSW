describe('Departamentos delete conflict 409', () => {
  const ADMIN_SESSION = {
    isAuthenticated: true,
    basicAuthToken: 'ZmFrZTpmYWtl',
    role: 'ROLE_ADMIN',
    email: 'admin@empresa.com',
    nombre: 'Admin'
  };

  it('muestra feedback cuando el backend responde 409 por integridad (T035)', () => {
    cy.intercept('GET', '**/api/v1/departamentos*', {
      content: [{ id: 1, nombre: 'Finanzas', totalEmpleados: 0 }],
      totalElements: 1,
      totalPages: 1,
      number: 0,
      size: 10,
      first: true,
      last: true,
      numberOfElements: 1,
      empty: false
    }).as('listDepartamentos');

    cy.intercept('DELETE', '**/api/v1/departamentos/1', {
      statusCode: 409,
      body: { message: 'No se puede eliminar: existen empleados asociados.' }
    }).as('deleteDepartamento');

    cy.visit('/login', {
      onBeforeLoad(win) {
        win.localStorage.setItem('auth.session', JSON.stringify(ADMIN_SESSION));
      }
    });

    cy.visit('/admin/departamentos');
    cy.url().should('include', '/admin/departamentos');

    cy.wait('@listDepartamentos');
    cy.on('window:confirm', () => true);
    cy.get('[data-cy="departamento-delete-1"]').click();
    cy.wait('@deleteDepartamento');
    cy.get('[data-cy="ui-toast"]').should('contain', 'No se puede eliminar: existen empleados asociados.');
  });
});

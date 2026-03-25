describe('Departamentos búsqueda local', () => {
  const ADMIN_SESSION = {
    isAuthenticated: true,
    basicAuthToken: 'ZmFrZTpmYWtl',
    role: 'ROLE_ADMIN',
    email: 'admin@empresa.com',
    nombre: 'Admin'
  };

  it('filtra resultados localmente por nombre con Signals (T026)', () => {
    cy.intercept('GET', '**/api/v1/departamentos*', {
      content: [
        { id: 1, nombre: 'Sistemas', totalEmpleados: 2 },
        { id: 2, nombre: 'Finanzas', totalEmpleados: 0 },
        { id: 3, nombre: 'Recursos Humanos', totalEmpleados: 1 }
      ],
      totalElements: 3,
      totalPages: 1,
      number: 0,
      size: 10,
      first: true,
      last: true,
      numberOfElements: 3,
      empty: false
    }).as('listDepartamentos');

    cy.visit('/login', {
      onBeforeLoad(win) {
        win.localStorage.setItem('auth.session', JSON.stringify(ADMIN_SESSION));
      }
    });

    cy.visit('/admin/departamentos');
    cy.url().should('include', '/admin/departamentos');

    cy.wait('@listDepartamentos');
    cy.get('[data-cy^="departamento-row-"]').should('have.length', 3);

    cy.get('[data-cy="departamentos-search"]').type('sis');
    cy.get('[data-cy^="departamento-row-"]').should('have.length', 1);
    cy.contains('td', 'Sistemas').should('be.visible');
    cy.contains('El filtro se aplica sobre la página cargada.').should('be.visible');

    cy.get('[data-cy="departamentos-search"]').clear();
    cy.get('[data-cy^="departamento-row-"]').should('have.length', 3);
    cy.contains('El filtro se aplica sobre la página cargada.').should('not.exist');
  });
});

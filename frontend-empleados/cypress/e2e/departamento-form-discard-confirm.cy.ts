describe('Departamento form discard confirm', () => {
  const ADMIN_SESSION = {
    isAuthenticated: true,
    basicAuthToken: 'ZmFrZTpmYWtl',
    role: 'ROLE_ADMIN',
    email: 'admin@empresa.com',
    nombre: 'Admin'
  };

  it('confirma descarte cuando el formulario está dirty (T047)', () => {
    cy.intercept('GET', '**/api/v1/departamentos*', {
      content: [{ id: 1, nombre: 'Sistemas', totalEmpleados: 2 }],
      totalElements: 1,
      totalPages: 1,
      number: 0,
      size: 10,
      first: true,
      last: true,
      numberOfElements: 1,
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
    cy.get('[data-cy="departamentos-new"]').click();
    cy.get('[data-cy="departamento-modal-nombre"]').type('Temporal');

    cy.window().then((win) => {
      cy.stub(win, 'confirm')
        .onFirstCall()
        .returns(false)
        .onSecondCall()
        .returns(true);
    });

    cy.get('[data-cy="departamento-modal-cancel"]').click();
    cy.get('[data-cy="departamento-modal"]').should('be.visible');

    cy.get('[data-cy="departamento-modal-cancel"]').click();
    cy.get('[data-cy="departamento-modal"]').should('not.exist');
  });
});

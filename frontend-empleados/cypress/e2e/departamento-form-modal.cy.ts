describe('Departamento form modal', () => {
  const ADMIN_SESSION = {
    isAuthenticated: true,
    basicAuthToken: 'ZmFrZTpmYWtl',
    role: 'ROLE_ADMIN',
    email: 'admin@empresa.com',
    nombre: 'Admin'
  };

  const page = {
    content: [{ id: 1, nombre: 'Sistemas', totalEmpleados: 1 }],
    totalElements: 1,
    totalPages: 1,
    number: 0,
    size: 10,
    first: true,
    last: true,
    numberOfElements: 1,
    empty: false
  };

  const visitAsAdmin = () => {
    cy.intercept('GET', '**/api/v1/departamentos*', page).as('listDepartamentos');

    cy.visit('/login', {
      onBeforeLoad(win) {
        win.localStorage.setItem('auth.session', JSON.stringify(ADMIN_SESSION));
      }
    });

    cy.visit('/admin/departamentos');
    cy.url().should('include', '/admin/departamentos');

    cy.wait('@listDepartamentos');
  };

  it('abre y cierra el modal en modos create/edit (T025)', () => {
    visitAsAdmin();

    cy.get('[data-cy="departamentos-new"]').click();
    cy.get('[data-cy="departamento-modal"]').should('be.visible');
    cy.get('#departamento-modal-title').should('contain', 'Crear departamento');

    cy.get('[data-cy="departamento-modal-cancel"]').click();
    cy.get('[data-cy="departamento-modal"]').should('not.exist');

    cy.get('[data-cy="departamento-edit-1"]').click();
    cy.get('[data-cy="departamento-modal"]').should('be.visible');
    cy.get('#departamento-modal-title').should('contain', 'Editar departamento');
    cy.get('[data-cy="departamento-modal-nombre"]').should('have.value', 'Sistemas');

    cy.get('[data-cy="departamento-modal-cancel"]').click();
    cy.get('[data-cy="departamento-modal"]').should('not.exist');
  });
});

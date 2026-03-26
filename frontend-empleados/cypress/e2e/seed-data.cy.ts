describe('Seed de datos maestro', () => {
  const loginAsAdmin = () => {
    cy.visit('/login');
    cy.get('#email').clear().type('admin@empresa.com');
    cy.get('#password').clear().type('admin123');
    cy.contains('button', 'Ingresar').click();
    cy.url({ timeout: 10000 }).should('include', '/admin');
  };

  const createDepartamento = (name: string) => {
    cy.get('[data-cy="departamentos-new"]').click();
    cy.get('[data-cy="departamento-modal"]').should('be.visible');
    cy.get('[data-cy="departamento-modal-nombre"]').clear().type(name);
    cy.get('[data-cy="departamento-modal-submit"]').click();
    cy.contains('Departamento creado exitosamente.', { timeout: 10000 }).should('be.visible');
    cy.get('[data-cy="departamento-modal"]').should('not.exist');
  };

  const ensureBaseDepartamentos = () => {
    loginAsAdmin();
    cy.visit('/admin/departamentos?page=0');
    cy.contains('h1', 'Gestión de departamentos').should('be.visible');
    cy.get('.skeleton-block', { timeout: 15000 }).should('not.exist');

    cy.get('body').then(($body) => {
      const hasRows = $body.find('[data-cy^="departamento-row-"]').length > 0;

      if (!hasRows) {
        createDepartamento('Sistemas');
        createDepartamento('Recursos Humanos');
      }
    });

    cy.get('[data-cy^="departamento-row-"]', { timeout: 10000 }).should('have.length.at.least', 1);
  };

  it('asegura departamentos, crea 5 empleados y valida 6 filas totales', () => {
    ensureBaseDepartamentos();

    cy.seedEmployees(5);

    let totalRows = 0;

    loginAsAdmin();
    cy.visit('/admin/empleados?page=0');
    cy.contains('h1', 'Gestión de empleados').should('be.visible');

    cy.get('table tbody tr', { timeout: 10000 })
      .should('have.length.at.least', 1)
      .then(($rows) => {
        totalRows += $rows.length;
      });

    cy.contains('button', 'Siguiente').then(($nextButton) => {
      if (!$nextButton.is(':disabled')) {
        cy.wrap($nextButton).click();
        cy.get('table tbody tr', { timeout: 10000 }).then(($rowsNextPage) => {
          totalRows += $rowsNextPage.length;
          expect(totalRows).to.equal(6);
        });
      } else {
        expect(totalRows).to.equal(6);
      }
    });
  });
});

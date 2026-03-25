describe('Departamentos CRUD base', () => {
  const ADMIN_SESSION = {
    isAuthenticated: true,
    basicAuthToken: 'ZmFrZTpmYWtl',
    role: 'ROLE_ADMIN',
    email: 'admin@empresa.com',
    nombre: 'Admin'
  };

  const visitAsAdmin = () => {
    cy.visit('/login', {
      onBeforeLoad(win) {
        win.localStorage.setItem('auth.session', JSON.stringify(ADMIN_SESSION));
      }
    });

    cy.visit('/admin/departamentos');
    cy.url().should('include', '/admin/departamentos');
  };

  it('carga la ruta de login como punto de entrada', () => {
    cy.visit('/login');
    cy.contains('Iniciar sesión').should('be.visible');
  });

  it('crea, lee, edita y elimina un departamento (T017)', () => {
    const responsePage = {
      content: [{ id: 1, nombre: 'Sistemas', totalEmpleados: 2 }],
      totalElements: 1,
      totalPages: 1,
      number: 0,
      size: 10,
      first: true,
      last: true,
      numberOfElements: 1,
      empty: false
    };

    cy.intercept('GET', '**/api/v1/departamentos*', (req) => {
      responsePage.totalElements = responsePage.content.length;
      responsePage.numberOfElements = responsePage.content.length;
      responsePage.empty = responsePage.content.length === 0;
      req.reply({ statusCode: 200, body: responsePage });
    }).as('listDepartamentos');

    cy.intercept('POST', '**/api/v1/departamentos', (req) => {
      const nextId = Math.max(0, ...responsePage.content.map((item) => item.id)) + 1;
      const created = { id: nextId, nombre: req.body.nombre, totalEmpleados: 0 };
      responsePage.content.push(created);
      req.reply({ statusCode: 201, body: created });
    }).as('createDepartamento');

    cy.intercept('PUT', '**/api/v1/departamentos/*', (req) => {
      const id = Number(req.url.split('/').pop());
      const index = responsePage.content.findIndex((item) => item.id === id);
      if (index >= 0) {
        responsePage.content[index] = {
          ...responsePage.content[index],
          nombre: req.body.nombre
        };
        req.reply({ statusCode: 200, body: responsePage.content[index] });
        return;
      }

      req.reply({ statusCode: 404, body: { message: 'Not found' } });
    }).as('updateDepartamento');

    cy.intercept('DELETE', '**/api/v1/departamentos/*', (req) => {
      const id = Number(req.url.split('/').pop());
      responsePage.content = responsePage.content.filter((item) => item.id !== id);
      req.reply({ statusCode: 204, body: '' });
    }).as('deleteDepartamento');

    visitAsAdmin();
    cy.wait('@listDepartamentos');

    cy.get('body').should('have.css', 'background-color', 'rgb(13, 17, 23)');
    cy.contains('h1', 'Gestión de departamentos').should('be.visible');
    cy.get('[data-cy="departamentos-table"]', { timeout: 10000 }).should('exist');
    cy.get('[data-cy="departamentos-new"]').click();
    cy.get('[data-cy="departamento-modal"]').should('be.visible');
    cy.get('[data-cy="departamento-modal-nombre"]').type('Finanzas');
    cy.get('[data-cy="departamento-modal-submit"]').click();

    cy.wait('@createDepartamento');
    cy.wait('@listDepartamentos');
    cy.get('[data-cy="ui-toast"]').should('contain', 'Departamento creado exitosamente');
    cy.contains('td', 'Finanzas').should('be.visible');

    cy.contains('tr', 'Finanzas').within(() => {
      cy.contains('button', 'Editar').click();
    });
    cy.get('[data-cy="departamento-modal-nombre"]').clear().type('Finanzas Global');
    cy.get('[data-cy="departamento-modal-submit"]').click();

    cy.wait('@updateDepartamento');
    cy.wait('@listDepartamentos');
    cy.get('[data-cy="ui-toast"]').should('contain', 'Departamento actualizado exitosamente');
    cy.contains('td', 'Finanzas Global').should('be.visible');

    cy.on('window:confirm', () => true);
    cy.contains('tr', 'Finanzas Global').within(() => {
      cy.contains('button', 'Eliminar').click();
    });

    cy.wait('@deleteDepartamento');
    cy.wait('@listDepartamentos');
    cy.get('[data-cy="ui-toast"]').should('contain', 'Departamento eliminado exitosamente');
    cy.contains('td', 'Finanzas Global').should('not.exist');
  });
});

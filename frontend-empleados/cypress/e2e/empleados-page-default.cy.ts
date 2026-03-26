describe('Empleados page param stability after create', () => {
  const ADMIN_SESSION = {
    isAuthenticated: true,
    basicAuthToken: 'ZmFrZTpmYWtl',
    role: 'ROLE_ADMIN',
    email: 'admin@empresa.com',
    nombre: 'Admin'
  };

  it('should always request page=0 and keep URL page=0 after creating an empleado', () => {
    const requestedPages: string[] = [];

    const empleadosPage = {
      content: [
        {
          clave: 'EMP-1001',
          nombre: 'Ana Base',
          email: 'ana.base@empresa.com',
          direccion: 'Calle 1',
          telefono: '555-0001',
          departamentoId: 1,
          rol: 'USER'
        }
      ],
      number: 0,
      size: 10,
      totalElements: 1,
      totalPages: 1,
      first: true,
      last: true,
      numberOfElements: 1,
      empty: false
    };

    cy.intercept('GET', '**/api/v1/empleados*', (req) => {
      requestedPages.push(String(req.query.page ?? ''));
      req.reply({ statusCode: 200, body: empleadosPage });
    }).as('listEmpleados');

    cy.intercept('GET', '**/api/v1/departamentos*', {
      statusCode: 200,
      body: {
        content: [{ id: 1, nombre: 'Sistemas' }],
        number: 0,
        size: 100,
        totalElements: 1,
        totalPages: 1,
        first: true,
        last: true,
        numberOfElements: 1,
        empty: false
      }
    }).as('listDepartamentos');

    cy.intercept('POST', '**/api/v1/empleados', {
      statusCode: 201,
      body: {
        clave: 'EMP-1002',
        nombre: 'Nuevo QA',
        email: 'nuevo.qa@empresa.com',
        direccion: 'Calle QA',
        telefono: '555-0099',
        departamentoId: 1,
        rol: 'USER'
      }
    }).as('createEmpleado');

    cy.visit('/login', {
      onBeforeLoad(win) {
        win.localStorage.setItem('auth.session', JSON.stringify(ADMIN_SESSION));
      }
    });

    cy.visit('/admin/empleados');
    cy.url().should('include', '/admin/empleados?page=0');

    cy.wait('@listEmpleados');
    cy.wait('@listDepartamentos');

    cy.contains('button', 'Nuevo empleado').click();

    cy.get('input[formcontrolname="nombre"]').type('Nuevo QA');
    cy.get('input[formcontrolname="email"]').type('nuevo.qa@empresa.com');
    cy.get('select[formcontrolname="departamentoId"]').select('Sistemas');
    cy.get('input[formcontrolname="direccion"]').type('Calle QA');
    cy.get('input[formcontrolname="telefono"]').type('555-0099');
    cy.get('input[formcontrolname="password"]').type('abc12345');

    cy.contains('button', 'Crear empleado').click();

    cy.wait('@createEmpleado');
    cy.wait('@listEmpleados');

    cy.wrap(null).then(() => {
      expect(requestedPages.length).to.be.greaterThan(1);
      requestedPages.forEach((page) => {
        expect(page).to.equal('0');
      });
    });

    cy.url().should('include', '/admin/empleados?page=0');
    cy.contains('Empleado creado exitosamente.').should('be.visible');
  });
});

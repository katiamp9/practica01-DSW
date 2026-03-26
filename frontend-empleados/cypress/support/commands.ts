import { fakerES } from '@faker-js/faker';

const loginAsAdmin = () => {
  cy.visit('/login');
  cy.get('#email').clear().type('admin@empresa.com');
  cy.get('#password').clear().type('admin123');
  cy.contains('button', 'Ingresar').click();
  cy.url({ timeout: 10000 }).should('include', '/admin');
};

Cypress.Commands.add('seedEmployees', (count = 5) => {
  loginAsAdmin();

  cy.visit('/admin/empleados?page=0');
  cy.contains('h1', 'Gestión de empleados').should('be.visible');

  Cypress._.times(count, (index) => {
    const firstName = fakerES.person.firstName();
    const lastName = fakerES.person.lastName();
    const fullName = `${firstName} ${lastName}`;
    const email = `${fakerES.string.alphanumeric(8).toLowerCase()}.${index}.${Date.now()}@empresa.com`;

    cy.contains('button', 'Nuevo empleado').click();
    cy.get('app-empleado-form').should('be.visible');

    cy.get('input[formcontrolname="nombre"]').should('be.enabled').clear({ force: true }).type(fullName, { force: true });
    cy.get('input[formcontrolname="email"]').should('be.enabled').clear({ force: true }).type(email, { force: true });

    cy.get('select[formcontrolname="departamentoId"] option').then((options) => {
      if (options.length < 2) {
        throw new Error('No hay departamentos disponibles para sembrar empleados. Crea al menos uno antes de usar cy.seedEmployees().');
      }
    });
    cy.get('select[formcontrolname="departamentoId"]').should('be.enabled').select(1, { force: true });

    cy.get('input[formcontrolname="direccion"]').should('be.enabled').clear({ force: true }).type(`Calle ${index + 1}`, { force: true });
    cy.get('input[formcontrolname="telefono"]').should('be.enabled').clear({ force: true }).type(`555000${(index + 1).toString().padStart(2, '0')}`, { force: true });
    cy.get('input[formcontrolname="password"]').should('be.enabled').clear({ force: true }).type('admin123', { force: true });

    cy.contains('button', 'Crear empleado').click();
    cy.contains('Empleado creado exitosamente.', { timeout: 10000 }).should('be.visible');
    cy.get('app-empleado-form').should('not.exist');
    cy.wait(500);
  });
});

declare global {
  namespace Cypress {
    interface Chainable {
      seedEmployees(count?: number): Chainable<void>;
    }
  }
}

export {};

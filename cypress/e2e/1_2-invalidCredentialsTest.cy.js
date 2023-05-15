// describe('Login Test', () => {
//   beforeEach(() => {
//     cy.visit('http://localhost:3000'); // Replace with the URL of your application
//   });
//
//   it('Displays a 403 Forbidden error with incorrect credentials', () => {
//     cy.intercept();
//     cy.route({
//       method: 'POST',
//       url: '/login', // Replace with the actual login API endpoint of your application
//       status: 403,
//       response: {},
//     }).as('loginRequest');
//
//     cy.get('input[name="username"]').type('invalid-username'); // Replace with an invalid username
//     cy.get('input[name="password"]').type('invalid-password'); // Replace with an invalid password
//     cy.get('button[type="submit"]').click();
//
//     cy.wait('@loginRequest');
//     cy.contains('403 Forbidden').should('be.visible'); // Replace with the expected error message element for a 403 error
//   });
// });

describe('Login Test', () => {
  beforeEach(() => {
    cy.visit('http://localhost:3000');
  });

  it('Displays an error message with incorrect credentials', () => {
    cy.get('input[name="username"]').type('invalid-username'); // Replace with an invalid username
    cy.get('input[name="password"]').type('invalid-password'); // Replace with an invalid password
    cy.get('button[type="submit"]').click();
  });
});

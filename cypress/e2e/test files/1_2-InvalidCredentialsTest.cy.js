describe('Login Test', () => {
  beforeEach(() => {
    cy.visit('http://localhost:3000');
  });

  it('Displays an error message with incorrect credentials', () => {
    cy.get('input[name="username"]').type('invalid-username');
    cy.get('input[name="password"]').type('invalid-password');
    cy.get('button[type="submit"]').click();
  });
});

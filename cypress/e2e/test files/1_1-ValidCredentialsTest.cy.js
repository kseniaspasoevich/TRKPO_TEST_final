describe('Login Test', () => {
  beforeEach(() => {
    cy.visit('http://localhost:3000');
  });

  it('Logs in with correct credentials', () => {
    cy.get('input[name="username"]').type('login');
    cy.get('input[name="password"]').type('password');
    cy.get('button[type="submit"]').click();

    cy.url().should('eq', 'http://localhost:3000/catalogue/root');
  });
});

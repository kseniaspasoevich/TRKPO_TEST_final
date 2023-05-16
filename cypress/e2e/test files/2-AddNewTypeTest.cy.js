
describe('Add new type', () => {
  it('should add new type when user click on button', () => {
    cy.visit('http://localhost:3000/type'); // Replace with the URL of your React app
    // Fill in the login form
    cy.get('input[name="username"]').type('login')
    cy.get('input[name="password"]').type('password')

    // Submit the login form
    cy.get('button[type="submit"]').click()
    // Click on the "Create" button to navigate to the create form
    cy.contains("Create").click();

    // Fill in the input field with the desired name
    cy.get('input[name="name"]').type("LONG");

    // Submit the form
    cy.contains("Save").click();
    cy.visit('http://localhost:3000/type')
  });
});

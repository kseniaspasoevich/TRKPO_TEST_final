
describe("Create User", () => {
  it("should create a new user", () => {
    cy.visit("http://localhost:3000/user");

    // Fill in the login form
    cy.get('input[name="username"]').type('login')
    cy.get('input[name="password"]').type('password')

    // Submit the login form
    cy.get('button[type="submit"]').click()

    // Click on the "Create" button to navigate to the user creation form
    cy.contains("Create").click();

    // Fill in the user details
    cy.get('input[name="login"]').type("ALINA");
    cy.get('input[name="password"]').type("ALINA");

    // Submit the form
    cy.contains("Save").click();

    // Wait for the user list to reload
    cy.contains("Loading...").should("not.exist");

    // Verify if the new user is displayed in the user list
    //cy.contains("newuser3").should("exist");
    cy.visit('http://localhost:3000/user')
  });
});
// Assuming you have installed Cypress and set it up already

// Assuming your React app's export button has a CSS selector of ".export-button"
// and the downloaded file has a CSS selector of ".user-list-file"

describe('Export Users', () => {
  it('should download the list of users when the export button is clicked', () => {
    cy.visit('http://localhost:3000/type'); // Replace with the URL of your React app
    // Fill in the login form
    cy.get('input[name="username"]').type('login')
    cy.get('input[name="password"]').type('password')

    // Submit the login form
    cy.get('button[type="submit"]').click()
    // Click on the "Create" button to navigate to the create form
    cy.contains("Create").click();

    // Fill in the input field with the desired name
    cy.get('input[name="name"]').type("object");

    // Submit the form
    cy.contains("Save").click();
    cy.visit('http://localhost:3000/type')

    // // Assert that the new type is added successfully
    // cy.contains("New Type").should("be.visible");
  });
});

describe("Document Type", () => {
  it("should edit a document type", () => {
    cy.visit("http://localhost:3000/type"); // Change the path to match the URL of your user list page

    // Fill in the login form
    cy.get('input[name="username"]').type('login')
    cy.get('input[name="password"]').type('password')

    // Submit the login form
    cy.get('button[type="submit"]').click()
    // Click on the Edit button
    cy.get("a.MuiButtonBase-root[aria-label='Edit']").first().click();


    // Update the document type name
    cy.get("input[name='name']").clear().type("NEW-TYPE");

    // Save the changes
    cy.contains("Save").click();

    // Verify that the document type is updated
    cy.contains("NEW-TYPE").should("exist");
  });
});

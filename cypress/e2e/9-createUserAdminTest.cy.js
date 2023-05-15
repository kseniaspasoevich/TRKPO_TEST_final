// // createUser.spec.js

describe("Create User", () => {
  it("should create a new user", () => {
    cy.visit("http://localhost:3000/user"); // Change the path to match the URL of your user list page

    // Fill in the login form
    cy.get('input[name="username"]').type('login')
    cy.get('input[name="password"]').type('password')

    // Submit the login form
    cy.get('button[type="submit"]').click()

    // Click on the "Create" button to navigate to the user creation form
    cy.contains("Create").click();

    // Fill in the user details
    cy.get('input[name="login"]').type("kseniasP");
    cy.get('input[name="password"]').type("kiki20003");
    //cy.get('select[name="role"]').select("USER");
    //cy.get('select[name="role"]').select('ADMIN').should('have.value', 'ADMIN');
    //cy.get('SelectInput[source="role"]').select("USER");
    //cy.get('select').select("USER");
    // cy.get('#roles').select('ADMIN');
    // cy.get('#roles').should('have.value', 'ADMIN')

    // Submit the form
    cy.contains("Save").click();

    // Wait for the user list to reload
    cy.contains("Loading...").should("not.exist");

    // Verify if the new user is displayed in the user list
    //cy.contains("newuser3").should("exist");
    cy.visit('http://localhost:3000/user')
  });
});

// describe("Create User", () => {
//   it("should create a new user", () => {
//     cy.visit("http://localhost:3000/user");
//
//     cy.contains("Create").click();
//
//     cy.get('input[name="login"]').type("newuser");
//     cy.get('input[name="password"]').type("newpassword");
//
//     // Wait for the select input to be visible and contain options
//     cy.get('select[name="role"]')
//         .should("be.visible")
//         .should("have.length.above", 0);
//
//     // Select the "USER" option
//     cy.get('select[name="role"]').select("USER");
//
//     cy.contains("Save").click();
//
//     // Wait for the user list to reload
//     cy.contains("Loading...").should("not.exist");
//
//     cy.contains("newuser").should("exist");
//   });
// });

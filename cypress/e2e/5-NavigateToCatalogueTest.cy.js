// // cypress/integration/navigation.spec.js

describe('Navigation', () => {
    beforeEach(() => {
        // Visit the login page before each test
        cy.visit('http://localhost:3000')
    })

    it('should navigate to a particular page after login', () => {
        // Fill in the login form
        cy.get('input[name="username"]').type('login')
        cy.get('input[name="password"]').type('password')

        // Submit the login form
        cy.get('button[type="submit"]').click()

        // Wait for the page to load after login
        cy.wait(2000) // Adjust the wait time as needed

        // Navigate to the desired page
        cy.visit('http://localhost:3000/root')
        cy.visit('http://localhost:3000/catalogue')

        // Assert that the desired page is loaded
        cy.contains('Catalogues') // Replace 'Page Title' with the expected content on the desired page
    })
})

// cypress/integration/navigation.spec.js

// describe('Navigation', () => {
//     beforeEach(() => {
//         // Visit the login page before each test
//         cy.visit('http://localhost:3000')
//     })
//
//     it('should navigate from page 1 to page 2 after login', () => {
//         // Fill in the login form
//         cy.get('input[name="username"]').type('login')
//         cy.get('input[name="password"]').type('password')
//
//         // Submit the login form
//         cy.get('button[type="submit"]').click()
//
//         // Wait for the page to load after login
//         cy.wait(2000) // Adjust the wait time as needed
//
//         // Assert that the user is on page 1 after login
//         cy.visit('http://localhost:3000/root') // Replace 'Page 1' with the expected content on page 1
//
//         // // Click on the link/button to navigate to page 2
//         // cy.get('http://localhost:3000/catalogue').click()
//         //
//         // // Wait for the page to load after navigation
//         // cy.wait(2000) // Adjust the wait time as needed
//         //
//         // // Assert that the user is on page 2
//         // cy.contains('Catalogue') // Replace 'Page 2' with the expected content on page 2
//     })
// })

// describe('Navigation', () => {
//     it('should navigate from page 1 to page 2 after successful login', () => {
//         cy.visit('http://localhost:3000'); // Visit the application URL
//
//         // Fill in the login form
//         cy.get('input[name="username"]').type('login');
//         cy.get('input[name="password"]').type('password');
//         cy.get('button[type="submit"]').click();
//
//         cy.url().should('eq', 'http://localhost:3000/catalogue');
//     });
// });

// describe('Navigation', () => {
//     it('should navigate from page 1 to page 2 after successful login', () => {
//         cy.visit('http://localhost:3000'); // Visit the application URL
//
//         // Fill in the login form
//         cy.get('input[name="username"]').type('login');
//         cy.get('input[name="password"]').type('password');
//         cy.get('button[type="submit"]').click();
//
//         // Wait for the login to be successful and redirect to /catalogue
//         cy.url().should('eq', 'http://localhost:3000/catalogue');
//         cy.get('h1').should('have.text', 'Catalogue'); // Assuming the heading on the catalogue page is an <h1> element
//     });
// });

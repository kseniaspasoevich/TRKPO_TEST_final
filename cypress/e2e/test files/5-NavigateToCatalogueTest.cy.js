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


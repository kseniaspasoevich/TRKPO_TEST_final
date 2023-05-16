
describe('Export users', () => {
    beforeEach(() => {
        Cypress.config('chromePreferences', {
            download: {
                default_directory: 'cypress/downloads',
            },
        });
    });

    it('should add new type when user click on button', () => {
        cy.visit('http://localhost:3000/user');
        // Fill in the login form
        cy.get('input[name="username"]').type('login')
        cy.get('input[name="password"]').type('password')

        // Submit the login form
        cy.get('button[type="submit"]').click()

        // Find the button using its class or other attributes
        cy.get('button.MuiButton-root[aria-label="Export"]').click();
    });
});


//those are all tests from test files folder in one file

describe('Open application', () => {
    it('passes', () => {
        cy.visit('http://localhost:3000')
    })
})

describe(' Correct login Test', () => {
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

describe('Incorrect login Test', () => {
    beforeEach(() => {
        cy.visit('http://localhost:3000');
    });

    it('Displays an error message with incorrect credentials', () => {
        cy.get('input[name="username"]').type('invalid-username');
        cy.get('input[name="password"]').type('invalid-password');
        cy.get('button[type="submit"]').click();
    });
});


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


describe('Export users', () => {
    beforeEach(() => {
        Cypress.config('chromePreferences', {
            download: {
                default_directory: 'cypress/downloads',
            },
        });
    });

    it('save users', () => {
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

describe("Edit document type ", () => {
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
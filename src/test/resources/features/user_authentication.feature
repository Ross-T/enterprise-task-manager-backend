Feature: User Authentication
  Enterprise users should be able to securely authenticate

  Scenario: Register a new user
    Given a user wants to register with username "testuser", email "test@example.com" and password "Password123"
    When the user submits registration details
    Then the user should be successfully registered
    And the password should be securely hashed in the database

  Scenario: Login with valid credentials
    Given a registered user with username "testuser" and password "Password123"
    When the user attempts to login
    Then the login should be successful
    And a JWT token should be generated

  Scenario: Login with invalid credentials
    Given a registered user with username "testuser" and password "Password123"
    When the user attempts to login with password "WrongPassword" 
    Then the login should fail with an authentication error
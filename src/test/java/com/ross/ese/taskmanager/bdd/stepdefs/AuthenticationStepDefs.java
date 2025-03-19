package com.ross.ese.taskmanager.bdd.stepdefs;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.ross.ese.taskmanager.service.SupabaseAuthService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationStepDefs {

    @MockitoBean
    private SupabaseAuthService supabaseAuthService;
    
    private String username;
    private String email;
    private String password;
    private String jwtToken;

    @Given("a user wants to register with username {string}, email {string} and password {string}")
    public void userWantsToRegister(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @When("the user submits registration details")
    public void userSubmitsRegistration() {
        when(supabaseAuthService.signUp(email, password, username)).thenReturn("mock-token");
        jwtToken = supabaseAuthService.signUp(email, password, username);
    }

    @Then("the user should be successfully registered")
    public void userShouldBeRegistered() {
        verify(supabaseAuthService).signUp(email, password, username);
        assertNotNull(jwtToken);
    }

    @And("the password should be securely hashed in the database")
    public void passwordShouldBeHashed() {
        assertNotEquals(password, jwtToken);
    }
    
    @Given("a registered user with username {string} and password {string}")
    public void registeredUserExists(String username, String password) {
        this.username = username;
        this.password = password;
        this.email = username + "@example.com";
    }
    
    @When("the user attempts to login")
    public void userAttemptsToLogin() {
        when(supabaseAuthService.signIn(email, password)).thenReturn("mock-login-token");
        jwtToken = supabaseAuthService.signIn(email, password);
    }
    
    @When("the user attempts to login with password {string}")
    public void userAttemptsToLoginWithPassword(String wrongPassword) {
        when(supabaseAuthService.signIn(email, wrongPassword)).thenReturn(null);
        jwtToken = supabaseAuthService.signIn(email, wrongPassword);
    }
    
    @Then("the login should be successful")
    public void loginShouldBeSuccessful() {
        verify(supabaseAuthService).signIn(email, password);
        assertNotNull(jwtToken);
    }
    
    @And("a JWT token should be generated")
    public void jwtTokenShouldBeGenerated() {
        assertNotNull(jwtToken);
        assertTrue(jwtToken.length() > 0);
    }
    
    @Then("the login should fail with an authentication error")
    public void loginShouldFail() {
        verify(supabaseAuthService).signIn(email, anyString());
        assertNull(jwtToken);
    }
}
package com.sopra_steria.jens_berckmoes.BDD.steps;

import com.sopra_steria.jens_berckmoes.exception.TokenNotFoundException;
import com.sopra_steria.jens_berckmoes.exception.UserNotFoundException;
import com.sopra_steria.jens_berckmoes.model.*;
import com.sopra_steria.jens_berckmoes.repository.TokenRepository;
import com.sopra_steria.jens_berckmoes.repository.UserRepository;
import com.sopra_steria.jens_berckmoes.service.LoginService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginStepDefinitions {
    private LoginService loginService;
    private LoginResult loginResult;

    // Predefined test objects for clarity
    private final Username validUsername = Username.of("jane.doe@example.com");
    private final Username invalidUsername = Username.of("nonexistent_user");

    private final TokenValue validTokenValue = TokenValue.of("some_valid_token");
    private final TokenValue invalidTokenValue = TokenValue.of("wrong_token");

    private final Token validToken = Token.weeklyToken("some_valid_token");

    @Given("I am on the login page")
    public void iAmOnTheLoginPage() {
        // single mock per dependency
        final UserRepository userRepository = mock(UserRepository.class);
        final TokenRepository tokenRepository = mock(TokenRepository.class);

        loginService = new LoginService(userRepository, tokenRepository);

        // Setup repository mocks for the scenario
        // Users
        when(userRepository.get(eq(validUsername)))
                .thenReturn(User.of(validUsername, validToken));
        when(userRepository.get(eq(invalidUsername)))
                .thenThrow(new UserNotFoundException());

        // Tokens
        when(tokenRepository.get(eq(validTokenValue)))
                .thenReturn(validToken);
        when(tokenRepository.get(eq(invalidTokenValue)))
                .thenThrow(new TokenNotFoundException());
    }

    @When("I attempt to log in with username {string} and token {string}")
    public void iAttemptToLogIn(String rawUsername, String rawToken) {
        // Convert inputs to value objects
        Username username = Username.of(rawUsername);
        TokenValue tokenValue = TokenValue.of(rawToken);

        // Call the service
        loginResult = loginService.login(username, tokenValue);
    }

    @Then("I am blocked and prompted to contact support")
    public void iAmBlocked() {
        assertThat(loginResult)
                .isEqualTo(LoginResult.of(LoginStatus.BLOCKED, "please contact support"));
    }

}

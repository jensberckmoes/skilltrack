package com.sopra_steria.jens_berckmoes.bdd.steps;

import com.sopra_steria.jens_berckmoes.bdd.fakes.InMemoryTokenRepository;
import com.sopra_steria.jens_berckmoes.bdd.fakes.InMemoryUserRepository;
import com.sopra_steria.jens_berckmoes.model.LoginResult;
import com.sopra_steria.jens_berckmoes.model.LoginStatus;
import com.sopra_steria.jens_berckmoes.model.TokenValue;
import com.sopra_steria.jens_berckmoes.model.Username;
import com.sopra_steria.jens_berckmoes.repository.TokenRepository;
import com.sopra_steria.jens_berckmoes.repository.UserRepository;
import com.sopra_steria.jens_berckmoes.service.LoginService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginStepDefinitions {
    private LoginService loginService;
    private LoginResult loginResult;

    @Given("I am on the login page")
    public void iAmOnTheLoginPage() {
        final TokenRepository tokenRepository = new InMemoryTokenRepository(LocalDateTime.of(2027, 1, 30, 16, 19, 0));
        final UserRepository userRepository = new InMemoryUserRepository(tokenRepository.getTokens());

        loginService = new LoginService(userRepository, tokenRepository);
    }

    @When("I attempt to log in with username {string} and token {string}")
    public void iAttemptToLogIn(final String rawUsername, final String rawToken) {
        final Username username = Username.of(rawUsername);
        final TokenValue tokenValue = TokenValue.of(rawToken);

        loginResult = loginService.login(username, tokenValue);
    }

    @Then("I am blocked and prompted to contact support")
    public void iAmBlocked() {
        assertThat(loginResult)
                .isEqualTo(LoginResult.of(LoginStatus.BLOCKED, "please contact support"));
    }

    @Then("I can successfully log in")
    public void iCanSuccessfullyLogIn() {
        assertThat(loginResult)
                .isEqualTo(LoginResult.success());
    }


}

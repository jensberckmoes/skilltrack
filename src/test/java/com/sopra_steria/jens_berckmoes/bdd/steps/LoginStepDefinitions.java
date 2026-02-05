package com.sopra_steria.jens_berckmoes.bdd.steps;

import com.sopra_steria.jens_berckmoes.domain.LoginResult;
import com.sopra_steria.jens_berckmoes.domain.valueobject.TokenValue;
import com.sopra_steria.jens_berckmoes.domain.valueobject.Username;
import com.sopra_steria.jens_berckmoes.service.LoginService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginStepDefinitions {
    @Autowired
    private LoginService loginService;

    private LoginResult loginResult;

    @SuppressWarnings("EmptyMethod")
    @Given("I am on the login page")
    public void iAmOnTheLoginPage() {
    }

    @When("I attempt to log in with username {string} and token {string}")
    public void iAttemptToLogIn(final String rawUsername, final String rawToken) {
        final Username username = Username.of(rawUsername);
        final TokenValue tokenValue = TokenValue.of(rawToken);

        loginResult = loginService.login(username, tokenValue);
    }

    @Then("I am blocked")
    public void iAmBlocked() {
        assertThat(loginResult)
                .isEqualTo(LoginResult.blocked());
    }

    @Then("I can successfully log in")
    public void iCanSuccessfullyLogIn() {
        assertThat(loginResult)
                .isEqualTo(LoginResult.success());
    }
}

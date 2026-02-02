package com.sopra_steria.jens_berckmoes.bdd.steps;

import com.sopra_steria.jens_berckmoes.config.TestConfig;
import com.sopra_steria.jens_berckmoes.model.LoginResult;
import com.sopra_steria.jens_berckmoes.model.LoginStatus;
import com.sopra_steria.jens_berckmoes.model.TokenValue;
import com.sopra_steria.jens_berckmoes.model.Username;
import com.sopra_steria.jens_berckmoes.service.LoginService;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginStepDefinitions {
    private LoginService loginService;
    private LoginResult loginResult;

    @Before
    public void setup(){
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class);
        this.loginService = context.getBean(LoginService.class);
    }

    @Given("I am on the login page")
    public void iAmOnTheLoginPage() {
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

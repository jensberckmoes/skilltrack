package com.sopra_steria.jens_berckmoes.BDD.steps;

import com.sopra_steria.jens_berckmoes.model.LoginResult;
import com.sopra_steria.jens_berckmoes.model.LoginStatus;
import com.sopra_steria.jens_berckmoes.repository.TokenRepository;
import com.sopra_steria.jens_berckmoes.repository.UserRepository;
import com.sopra_steria.jens_berckmoes.service.LoginService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginStepDefinitions {
    private LoginService loginService;
    private LoginResult loginResult;

    @Given("I am on the login page")
    public void iAmOnTheLoginPage() {
        loginService = new LoginService(new UserRepository(), new TokenRepository());
    }

    @When("I enter username {string} and token {string}")
    public void iEnterUsernameAndToken(final String username, final String tokenValue) {
        loginResult = loginService.login(username, tokenValue);
    }

    @Then("I am blocked and prompted to contact support")
    public void iAmBlockedAndPromptedToContactSupport() {
        assertThat(loginResult).isEqualTo(LoginResult.of(LoginStatus.BLOCKED, "please contact support"));
    }

}

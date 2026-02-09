package com.sopra_steria.jens_berckmoes.bdd.steps;

import com.sopra_steria.jens_berckmoes.controller.UserController;
import com.sopra_steria.jens_berckmoes.domain.dto.UserDto;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static com.sopra_steria.jens_berckmoes.TestConstants.Users.BDD_USERS;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("User Feature CRUD Operations Step Definitions")
public class UserStepDefinitions {
    @Autowired private UserController userController;
    private Set<UserDto> users;

    @When("I browse to get all users")
    public void iCallGETApiUsers() {
        users = userController.getAllUsers();
    }

    @Then("the response contains a list of all the users")
    public void theResponseShouldContainAllUsers() {
        assertThat(users).containsExactlyInAnyOrder(BDD_USERS.keySet()
                .stream()
                .map(UserDto::of)
                .distinct()
                .toArray(UserDto[]::new));
    }

}

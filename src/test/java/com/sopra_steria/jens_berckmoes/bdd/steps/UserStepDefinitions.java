package com.sopra_steria.jens_berckmoes.bdd.steps;

import com.sopra_steria.jens_berckmoes.bdd.fakes.StepResult;
import com.sopra_steria.jens_berckmoes.controller.UserController;
import com.sopra_steria.jens_berckmoes.domain.dto.CreateUserRequest;
import com.sopra_steria.jens_berckmoes.domain.dto.GetAllUsersResponse;
import com.sopra_steria.jens_berckmoes.domain.dto.GetUserResponse;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Callable;

import static com.sopra_steria.jens_berckmoes.TestConstants.GetUserResponses.GET_USER_RESPONSES_AS_SET;
import static com.sopra_steria.jens_berckmoes.TestConstants.Users.ALICE_USERNAME_RAW_STRING;
import static com.sopra_steria.jens_berckmoes.bdd.fakes.StepResult.callController;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("User Feature CRUD Operations Step Definitions")
public class UserStepDefinitions {
    @Autowired
    private UserController userController;

    private StepResult<GetAllUsersResponse> getUsersResult;
    private StepResult<GetUserResponse> getUserResult;

    @When("I browse to get all users and users exist in the database")
    public void iCallGETApiUsers() {
        getUsersResult = callController(() -> userController.getAllUsers().getBody());
    }

    @Then("the response contains a list of all the users")
    public void theResponseShouldContainAllUsers() {
        assertThat(getUsersResult.isSuccess()).isTrue();
        assertThat(getUsersResult.body()).isNotNull();
        assertThat(getUsersResult.body().getUserResponses()).containsExactlyInAnyOrderElementsOf(GET_USER_RESPONSES_AS_SET);
    }

    @When("I browse to get all users but none are in the database")
    public void iBrowseToGetAllUsersButNoneAreFound() {
        getUsersResult = callController(() -> userController.getAllUsers().getBody());
    }

    @Then("the response contains a message declaring that no users were found")
    public void theResponseContainsAMessageDeclaringThatNoUsersWereFound() {
        assertThat(getUsersResult.exceptionMessage()).isEqualTo("No users found in the database.");
    }

    @When("I browse to get a user with username {string}")
    public void iBrowseToGetAUserWithUsername(final String username) {
        getUserResult = callController(() -> userController.getUserByUsername(username).getBody());
    }

    @When("I browse to get a user with no username")
    public void iBrowseToGetAUserWithNoUsername() {
        getUserResult = callController(() -> userController.getUserByUsername(null).getBody());
    }

    @Then("the response contains the user details")
    public void theResponseContainsTheUserDetails() {
        assertThat(getUserResult.isSuccess()).isTrue();
        assertThat(getUserResult.body()).isNotNull();
        assertThat(getUserResult.body().username()).isEqualTo(ALICE_USERNAME_RAW_STRING);
    }

    @Then("the response contains a message {string}")
    public void theResponseContainsAMessage(final String arg0) {
        assertThat(getUserResult.exceptionMessage()).isEqualTo(arg0);
    }

    @When("I browse to create a user with username {string}")
    public void iBrowseToCreateAUserWithUsername(final String arg0) {
        final GetUserResponse body = userController.createUser(CreateUserRequest.of(arg0)).getBody();
        getUserResult = StepResult.success(body);
    }

    @Then("the response contains the created username {string}")
    public void theResponseContainsTheCreatedUserDetails(final String username) {
        assertThat(getUserResult.isSuccess()).isTrue();
        assertThat(getUserResult.body()).isNotNull();
        assertThat(getUserResult.body().username()).isEqualTo(username);
    }

}

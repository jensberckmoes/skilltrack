package com.sopra_steria.jens_berckmoes.bdd.steps;

import com.sopra_steria.jens_berckmoes.controller.UserController;
import com.sopra_steria.jens_berckmoes.domain.dto.CreateUserRequest;
import com.sopra_steria.jens_berckmoes.domain.dto.GetUserResponse;
import com.sopra_steria.jens_berckmoes.domain.dto.GetAllUsersResponse;
import com.sopra_steria.jens_berckmoes.domain.exception.NoUsersFoundException;
import com.sopra_steria.jens_berckmoes.domain.exception.UserNotFoundException;
import com.sopra_steria.jens_berckmoes.domain.exception.UsernameRawValueNullOrBlankException;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import static com.sopra_steria.jens_berckmoes.TestConstants.Users.BDD_USERS;
import static com.sopra_steria.jens_berckmoes.domain.mapping.UserDtoMapper.toGetUsersResponse;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("User Feature CRUD Operations Step Definitions")
public class UserStepDefinitions {
    @Autowired private UserController userController;

    private ResponseEntity<GetAllUsersResponse> users;
    private ResponseEntity<GetUserResponse> user;

    private String exceptionMessage;

    @When("I browse to get all users and users exist in the database")
    public void iCallGETApiUsers() {
        users = userController.getAllUsers();
    }

    @Then("the response contains a list of all the users")
    public void theResponseShouldContainAllUsers() {
        assertThat(users.getBody()).isNotNull();
        assertThat(users.getBody()
                .getUserResponses()).containsExactlyInAnyOrder(toGetUsersResponse(BDD_USERS.values()).toArray(new GetUserResponse[0]));
    }

    @When("I browse to get all users but none are in the database")
    public void iBrowseToGetAllUsersButNoneAreFound() {
        try {
            users = userController.getAllUsers();
        } catch(final NoUsersFoundException e) {
            exceptionMessage = e.getMessage();
        }

    }

    @Then("the response contains a message declaring that no users were found")
    public void theResponseContainsAMessageDeclaringThatNoUsersWereFound() {
        assertThat(exceptionMessage).isEqualTo("No users found in the database.");
    }

    @When("I browse to get a user with username {string}")
    public void iBrowseToGetAUserWithUsername(final String arg0) {
        try {
            user = userController.getUserByUsername(arg0);
        } catch(final UsernameRawValueNullOrBlankException | UserNotFoundException e) {
            exceptionMessage = e.getMessage();
        }
    }

    @When("I browse to get a user with no username")
    public void iBrowseToGetAUserWithNoUsername() {
        try {
            user = userController.getUserByUsername(null);
        } catch(final UsernameRawValueNullOrBlankException e) {
            exceptionMessage = e.getMessage();
        }
    }

    @Then("the response contains the user details")
    public void theResponseContainsTheUserDetails() {
        assertThat(user).isNotNull();
        assertThat(user.getBody()).isNotNull();
        assertThat(user.getBody().username()).isEqualTo("jane.doe@example.com");
    }

    @Then("the response contains a message {string}")
    public void theResponseContainsAMessage(final String arg0) {
        assertThat(exceptionMessage).isEqualTo(arg0);
    }

    @When("I browse to create a user with username {string}")
    public void iBrowseToCreateAUserWithUsername(final String arg0) {
        user = userController.createUser(CreateUserRequest.of(arg0));
    }

    @Then("the response contains the created user details")
    public void theResponseContainsTheCreatedUserDetails() {
        assertThat(user).isNotNull();
        assertThat(user.getBody()).isNotNull();
        assertThat(user.getBody().username()).isEqualTo("josken.vermeulen@gmail.com");
    }
}

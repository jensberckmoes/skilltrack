package com.sopra_steria.jens_berckmoes.bdd.steps;

import com.sopra_steria.jens_berckmoes.controller.UserController;
import com.sopra_steria.jens_berckmoes.domain.dto.UserDto;
import com.sopra_steria.jens_berckmoes.domain.dto.UserDtoResponse;
import com.sopra_steria.jens_berckmoes.domain.exception.NoUsersFoundException;
import com.sopra_steria.jens_berckmoes.domain.exception.UsernameRawValueNullOrBlankException;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import static com.sopra_steria.jens_berckmoes.TestConstants.BLANK;
import static com.sopra_steria.jens_berckmoes.TestConstants.Users.BDD_USERS;
import static com.sopra_steria.jens_berckmoes.domain.mapping.UserDtoMapper.toDtos;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("User Feature CRUD Operations Step Definitions")
public class UserStepDefinitions {
    @Autowired private UserController userController;

    private ResponseEntity<UserDtoResponse> users;
    private ResponseEntity<UserDto> user;

    private RuntimeException exception;

    @When("I browse to get all users")
    public void iCallGETApiUsers() {
        users = userController.getAllUsers();
    }

    @Then("the response contains a list of all the users")
    public void theResponseShouldContainAllUsers() {
        assertThat(users.getBody()).isNotNull();
        assertThat(users.getBody()
                .userDtos()).containsExactlyInAnyOrder(toDtos(BDD_USERS.values()).toArray(new UserDto[0]));
    }

    @When("I browse to get all users but none are found")
    public void iBrowseToGetAllUsersButNoneAreFound() {
        try {
            users = userController.getAllUsers();
        } catch(final NoUsersFoundException e) {
            exception = e;
        }

    }

    @Then("the response contains a message declaring that no users were found")
    public void theResponseContainsAMessageDeclaringThatNoUsersWereFound() {
        assertThat(exception).isInstanceOf(NoUsersFoundException.class);
    }

    @When("I browse to get a user by username")
    public void iBrowseToGetAUserById() {
        user = userController.getUserByUsername("jane.doe@example.com");
    }

    @Then("the response contains the user details")
    public void theResponseContainsTheUserDetails() {
        assertThat(user).isNotNull();
        assertThat(user.getBody()).isNotNull();
        assertThat(user.getBody().username()).isEqualTo("jane.doe@example.com");
    }

    @When("I browse to get a user by empty username but none are found")
    public void iBrowseToGetAUserByEmptyUsernameButNoneAreFound() {
        try {
            user = userController.getUserByUsername(BLANK);
        } catch(final UsernameRawValueNullOrBlankException e) {
            exception = e;
        }
    }

    @Then("the response contains a message declaring that the request was invalid")
    public void theResponseContainsAMessageDeclaringThatTheRequestWasInvalid() {
        assertThat(exception).isInstanceOf(UsernameRawValueNullOrBlankException.class);
    }
}

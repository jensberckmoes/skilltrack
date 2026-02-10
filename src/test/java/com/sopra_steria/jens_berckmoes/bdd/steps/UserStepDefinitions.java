package com.sopra_steria.jens_berckmoes.bdd.steps;

import com.sopra_steria.jens_berckmoes.bdd.fakes.InMemoryUserRepository;
import com.sopra_steria.jens_berckmoes.controller.UserController;
import com.sopra_steria.jens_berckmoes.domain.dto.UserDto;
import com.sopra_steria.jens_berckmoes.domain.dto.UserDtoResponse;
import com.sopra_steria.jens_berckmoes.domain.exception.NoUsersFoundException;
import com.sopra_steria.jens_berckmoes.infra.entity.UserEntity;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.Set;

import static com.sopra_steria.jens_berckmoes.TestConstants.Users.BDD_USERS;
import static com.sopra_steria.jens_berckmoes.domain.mapping.UserDtoMapper.toDtos;
import static com.sopra_steria.jens_berckmoes.infra.mapping.UserMapper.mapToInfra;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DisplayName("User Feature CRUD Operations Step Definitions")
public class UserStepDefinitions {
    @Autowired private UserController userController;
    @Autowired private InMemoryUserRepository userRepository;

    private ResponseEntity<UserDtoResponse> users;

    @AfterEach
    void teardown() {
        wipeDatabaseClean();
    }

    @BeforeEach
    void setup() {
        wipeDatabaseClean();
        setUpTestUsers();
    }

    private void wipeDatabaseClean() {
        userRepository.deleteAll();
    }

    private void setUpTestUsers() {
        final Set<UserEntity> userEntities = mapToInfra(BDD_USERS.values());
        userRepository.saveAll(userEntities);
    }

    @When("I browse to get all users")
    public void iCallGETApiUsers() {
        users = userController.getAllUsers();
    }

    @Then("the response contains a list of all the users")
    public void theResponseShouldContainAllUsers() {
        assertThat(users).isNotNull();
        assertThat(users.getStatusCode()).isEqualTo(org.springframework.http.HttpStatus.OK);
        assertThat(users.getBody()).isNotNull();
        assertThat(users.getBody()
                .userDtos()).containsExactlyInAnyOrder(toDtos(BDD_USERS.values()).toArray(new UserDto[0]));
    }

    @When("I browse to get all users but none are found")
    public void iBrowseToGetAllUsersButNoneAreFound() {
        wipeDatabaseClean();
    }

    @Then("the response contains a message declaring that no users were found")
    public void theResponseContainsAMessageDeclaringThatNoUsersWereFound() {
        assertThatThrownBy(() -> userController.getAllUsers()).isInstanceOf(NoUsersFoundException.class);
    }
}

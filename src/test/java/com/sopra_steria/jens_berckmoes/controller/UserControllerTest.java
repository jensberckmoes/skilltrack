package com.sopra_steria.jens_berckmoes.controller;

import com.sopra_steria.jens_berckmoes.bdd.fakes.StepResult;
import com.sopra_steria.jens_berckmoes.domain.dto.GetAllUsersResponse;
import com.sopra_steria.jens_berckmoes.domain.dto.GetUserResponse;
import com.sopra_steria.jens_berckmoes.domain.exception.NoUsersFoundException;
import com.sopra_steria.jens_berckmoes.domain.exception.UserNotFoundException;
import com.sopra_steria.jens_berckmoes.domain.mapping.UserDtoMapper;
import com.sopra_steria.jens_berckmoes.domain.repository.TokenRepository;
import com.sopra_steria.jens_berckmoes.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;

import static com.sopra_steria.jens_berckmoes.TestConstants.Usernames.ALICE_USERNAME;
import static com.sopra_steria.jens_berckmoes.TestConstants.Usernames.NON_EXISTING_USERNAME;
import static com.sopra_steria.jens_berckmoes.TestConstants.Users.ALICE;
import static com.sopra_steria.jens_berckmoes.TestConstants.Users.USERS_AS_SET;
import static com.sopra_steria.jens_berckmoes.domain.mapping.UserDtoMapper.toGetUserResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;

@DisplayName("User controller")
class UserControllerTest {

    final UserRepository userRepository = mock(UserRepository.class);
    final UserController userController = new UserController(userRepository, mock(TokenRepository.class));

    @Test
    @DisplayName("should get all users as user dto objects with status code 200 OK")
    void shouldGetAllUsers() {
        // Assess
        when(userRepository.findAll()).thenReturn(USERS_AS_SET);

        // Act
        final StepResult<ResponseEntity<GetAllUsersResponse>> response = StepResult.callController(userController::getAllUsers);

        // Assert
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.body().getStatusCode()).isEqualTo(OK);
        assertThat(response.body().getBody()).isNotNull();
        assertThat(response.body().getBody().getUserResponses()).containsExactlyInAnyOrderElementsOf(UserDtoMapper.toGetUsersResponse(USERS_AS_SET));
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("should get a NoUsersFoundException when no users found and a 204 No Content status code")
    void shouldGetANoUsersFoundExceptionWhenNoUsersFound() {
        // Assess
        when(userRepository.findAll()).thenReturn(new HashSet<>());

        final StepResult<ResponseEntity<GetAllUsersResponse>> response = StepResult.callController(userController::getAllUsers);

        // Act
        assertThat(response.isFailure()).isTrue();
        assertThat(response.exception()).isInstanceOf(NoUsersFoundException.class);
        assertThat(response.exception().getMessage()).isEqualTo("No users found in the database.");
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("should be able to get a User using username with status code 200 OK")
    void shouldBeAbleToGetUserByUsername() {
        // Assess
        when(userRepository.findByUsername(ALICE_USERNAME)).thenReturn(ALICE);

        // Act
        final StepResult<ResponseEntity<GetUserResponse>> response = StepResult.callController(() -> userController.getUserByUsername(ALICE.username()));

        // Assert
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.body().getStatusCode()).isEqualTo(OK);
        assertThat(response.body().getBody()).isNotNull();
        assertThat(response.body().getBody()).isEqualTo(toGetUserResponse(ALICE));
        verify(userRepository, times(1)).findByUsername(ALICE_USERNAME);
    }

    @Test
    @DisplayName("should get a 404 Not found status code when trying to get a non existing user")
    void shouldGetNotFoundWhenNonExistingUsername() {
        // Assess
        when(userRepository.findByUsername(NON_EXISTING_USERNAME)).thenThrow(new UserNotFoundException("User not found: " + NON_EXISTING_USERNAME));

        final StepResult<ResponseEntity<GetUserResponse>> response = StepResult.callController(() -> userController.getUserByUsername(NON_EXISTING_USERNAME.value()));

        // Act + Assert
        assertThat(response.isFailure()).isTrue();
        assertThat(response.exception()).isInstanceOf(UserNotFoundException.class);
        assertThat(response.exception().getMessage()).isEqualTo("User not found: " + NON_EXISTING_USERNAME);
        verify(userRepository, times(1)).findByUsername(NON_EXISTING_USERNAME);
    }

}
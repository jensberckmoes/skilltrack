package com.sopra_steria.jens_berckmoes.controller;

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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

@DisplayName("User controller")
class UserControllerTest {

    final UserRepository userRepository = mock(UserRepository.class);
    final TokenRepository tokenRepository = mock(TokenRepository.class);
    final UserController userController = new UserController(userRepository, tokenRepository);

    @Test
    @DisplayName("should get all users as user dto objects with status code 200 OK")
    void shouldGetAllUsers() {
        // Assess
        when(userRepository.findAll()).thenReturn(USERS_AS_SET);

        // Act
        final var response = userController.getAllUsers();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUserResponses()).containsExactlyInAnyOrderElementsOf(UserDtoMapper.toGetUsersResponse(USERS_AS_SET));
    }

    @Test
    @DisplayName("should get a NoUsersFoundException when no users found and a 204 No Content status code")
    void shouldGetANoUsersFoundExceptionWhenNoUsersFound() {
        // Assess
        when(userRepository.findAll()).thenReturn(new HashSet<>());

        // Act
        assertThatThrownBy(userController::getAllUsers).isInstanceOf(NoUsersFoundException.class);
    }

    @Test
    @DisplayName("should be able to get a User using username with status code 200 OK")
    void shouldBeAbleToGetUserByUsername() {
        // Assess
        when(userRepository.findByUsername(ALICE_USERNAME)).thenReturn(ALICE);

        // Act
        final ResponseEntity<GetUserResponse> response = userController.getUserByUsername(ALICE.username());

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isEqualTo(toGetUserResponse(ALICE));
    }

    @Test
    @DisplayName("should get a 404 Not found status code when trying to get a non existing user")
    void shouldGetNotFoundWhenNonExistingUsername() {
        // Assess
        when(userRepository.findByUsername(NON_EXISTING_USERNAME)).thenThrow(UserNotFoundException.class);

        // Act + Assert
        assertThatThrownBy(() -> userController.getUserByUsername(NON_EXISTING_USERNAME.value())).isInstanceOf(
                UserNotFoundException.class);
    }

}
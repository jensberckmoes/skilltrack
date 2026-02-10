package com.sopra_steria.jens_berckmoes.controller;

import com.sopra_steria.jens_berckmoes.domain.dto.UserDto;
import com.sopra_steria.jens_berckmoes.domain.exception.NoUsersFoundException;
import com.sopra_steria.jens_berckmoes.domain.mapping.UserDtoMapper;
import com.sopra_steria.jens_berckmoes.domain.repository.TokenRepository;
import com.sopra_steria.jens_berckmoes.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static com.sopra_steria.jens_berckmoes.TestConstants.Users.USERS_AS_SET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

@DisplayName(("User controller"))
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
        assertThat(response.getBody().userDtos()).containsExactlyInAnyOrder(UserDtoMapper.toDtos(USERS_AS_SET)
                .toArray(new UserDto[0]));
    }

    @Test
    @DisplayName("should get a NoUsersFoundException when no users found and a 204 No Content status code")
    void shouldGetANoUsersFoundExceptionWhenNoUsersFound() {
        // Assess
        when(userRepository.findAll()).thenReturn(new HashSet<>());

        // Act
        assertThatThrownBy(userController::getAllUsers).isInstanceOf(NoUsersFoundException.class);
    }
}
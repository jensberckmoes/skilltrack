package com.sopra_steria.jens_berckmoes.service;

import com.sopra_steria.jens_berckmoes.model.LoginResult;
import com.sopra_steria.jens_berckmoes.model.LoginStatus;
import com.sopra_steria.jens_berckmoes.model.Token;
import com.sopra_steria.jens_berckmoes.model.User;
import com.sopra_steria.jens_berckmoes.repository.TokenRepository;
import com.sopra_steria.jens_berckmoes.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoginServiceTest {
    private final UserRepository userRepository = mock(UserRepository.class);
    private final TokenRepository tokenRepository = mock(TokenRepository.class);
    private final LoginService loginService = new LoginService(userRepository, tokenRepository);

    @ParameterizedTest
    @MethodSource("provideInputForInputIsNullOrBlank")
    void shouldBlockWhenInputIsNullOrBlank(final String userName, final String token) {
        //Arrange
        when(userRepository.exists(userName)).thenReturn(true);
        when(tokenRepository.exists(token)).thenReturn(true);

        //Act
        final LoginResult result = loginService.login(userName, token);

        //Assert
        assertThat(result)
                .as("Checking loginResult for username %s and token %s", userName, token)
                .isEqualTo(LoginResult.of(LoginStatus.BLOCKED, "please contact support"));
    }

    public static Stream<Arguments> provideInputForInputIsNullOrBlank() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of("", null),
                Arguments.of("  ", null),
                Arguments.of(null, ""),
                Arguments.of(null, " "));
    }

    @Test
    void shouldBlockWhenUsernameIsInvalid() {
        //Arrange
        final String invalidUsername = "nonexistent_user";
        final String validToken = "valid_token";
        final Token dbToken = Token.of(validToken, LocalDateTime.now().plusDays(5));
        when(tokenRepository.exists(dbToken.token())).thenReturn(true);
        when(tokenRepository.get(dbToken.token())).thenReturn(dbToken);
        when(userRepository.exists(invalidUsername)).thenReturn(false);

        //Act
        final LoginResult result = loginService.login(invalidUsername, validToken);

        //Assert
        assertThat(result)
                .isEqualTo(LoginResult.of(LoginStatus.BLOCKED, "please contact support"));
    }

    @Test
    void shouldBlockWhenTokenIsInvalid() {
        //Arrange
        final String validUsername = "jane.doe@example.com";
        final String invalidToken = "invalid_token";
        final User dbUser = User.of(validUsername, "some_valid_token");
        when(userRepository.exists(dbUser.token())).thenReturn(true);
        when(userRepository.get(dbUser.username())).thenReturn(dbUser);
        when(tokenRepository.exists(invalidToken)).thenReturn(false);

        //Act
        final LoginResult result = loginService.login(validUsername, invalidToken);

        //Assert
        assertThat(result)
                .isEqualTo(LoginResult.of(LoginStatus.BLOCKED, "please contact support"));
    }

}
package com.sopra_steria.jens_berckmoes.service;

import com.sopra_steria.jens_berckmoes.exception.TokenNotFoundException;
import com.sopra_steria.jens_berckmoes.exception.UserNotFoundException;
import com.sopra_steria.jens_berckmoes.model.*;
import com.sopra_steria.jens_berckmoes.repository.TokenRepository;
import com.sopra_steria.jens_berckmoes.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoginServiceTest {
    private final UserRepository userRepository = mock(UserRepository.class);
    private final TokenRepository tokenRepository = mock(TokenRepository.class);
    private final LoginService loginService = new LoginService(userRepository, tokenRepository);

    @Test
    void shouldBlockWhenUsernameIsInvalid() {
        //Arrange
        final String validToken = "some_valid_token";
        final Username username = Username.of("nonexistent_user");
        final TokenValue tokenValue = TokenValue.of(validToken);
        final Token token = Token.of(validToken, LocalDateTime.of(2026, 1, 29, 17, 10, 0));
        when(userRepository.get(username)).thenThrow(new UserNotFoundException());
        when(tokenRepository.get(tokenValue)).thenReturn(token);

        //Act
        final LoginResult result = loginService.login(username, tokenValue);

        //Assert
        assertThat(result)
                .isEqualTo(LoginResult.of(LoginStatus.BLOCKED, "please contact support"));
    }

    @Test
    void shouldBlockWhenTokenIsInvalid() {
        //Arrange
        final String validUsername = "jane.doe@example.com";
        final String invalidToken = "invalid_token";
        final Username username = Username.of(validUsername);
        final TokenValue token = TokenValue.of(invalidToken);
        when(userRepository.get(username)).thenReturn(User.defaultUser(username));
        when(tokenRepository.get(token)).thenThrow(new TokenNotFoundException());

        //Act
        final LoginResult result = loginService.login(username, token);

        //Assert
        assertThat(result)
                .isEqualTo(LoginResult.of(LoginStatus.BLOCKED, "please contact support"));
    }

    @Test
    void shouldBlockWhenTokenDoesNotBelongToUser() {
        //Arrange
        final String validTokenName = "some_valid_token";
        final Username username = Username.of("nonexistent_user");
        final TokenValue tokenValue = TokenValue.of(validTokenName);
        final Token otherToken = Token.of(validTokenName, LocalDateTime.of(2026, 1, 29, 17, 10, 0).plusDays(1));
        final Token token = Token.of(validTokenName, LocalDateTime.of(2026, 1, 29, 17, 10, 0));
        when(userRepository.get(username)).thenReturn(User.of(username, token));
        when(tokenRepository.get(tokenValue)).thenReturn(otherToken);

        //Act
        final LoginResult result = loginService.login(username, tokenValue);

        //Assert
        assertThat(result)
                .isEqualTo(LoginResult.of(LoginStatus.BLOCKED, "please contact support"));
    }

}
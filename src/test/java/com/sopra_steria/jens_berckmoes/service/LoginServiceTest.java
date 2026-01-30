package com.sopra_steria.jens_berckmoes.service;

import com.sopra_steria.jens_berckmoes.exception.TokenNotFoundException;
import com.sopra_steria.jens_berckmoes.exception.UserNotFoundException;
import com.sopra_steria.jens_berckmoes.model.*;
import com.sopra_steria.jens_berckmoes.repository.TokenRepository;
import com.sopra_steria.jens_berckmoes.repository.UserRepository;
import org.junit.jupiter.api.Test;

import static com.sopra_steria.jens_berckmoes.TestConstants.Tokens.*;
import static com.sopra_steria.jens_berckmoes.TestConstants.Users.INVALID_USERNAME;
import static com.sopra_steria.jens_berckmoes.TestConstants.Users.VALID_USERNAME;
import static com.sopra_steria.jens_berckmoes.model.LoginResult.blocked;
import static com.sopra_steria.jens_berckmoes.model.LoginResult.success;
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
        final Username invalidUsername = Username.of(INVALID_USERNAME);
        final TokenValue validTokenValue = TokenValue.of(VALID_TOKEN);

        when(userRepository.get(invalidUsername)).thenThrow(new UserNotFoundException());
        when(tokenRepository.get(validTokenValue)).thenReturn(Token.of(validTokenValue.value(), STATIC_NOW));

        //Act
        final LoginResult result = loginService.login(invalidUsername, validTokenValue);

        //Assert
        assertThat(result).isEqualTo(blocked());
    }

    @Test
    void shouldBlockWhenTokenIsInvalid() {
        //Arrange
        final Username validUsername = Username.of(VALID_USERNAME);
        final TokenValue invalidTokenValue = TokenValue.of(WRONG_TOKEN);

        when(userRepository.get(validUsername)).thenReturn(User.of(validUsername, Token.of(invalidTokenValue.value(), STATIC_NOW)));
        when(tokenRepository.get(invalidTokenValue)).thenThrow(new TokenNotFoundException());

        //Act
        final LoginResult result = loginService.login(validUsername, invalidTokenValue);

        //Assert
        assertThat(result).isEqualTo(blocked());
    }

    @Test
    void shouldBlockWhenTokenDoesNotBelongToUser() {
        //Arrange
        final Username validUsername = Username.of(VALID_USERNAME);
        final TokenValue validTokenValue = TokenValue.of(VALID_TOKEN);

        when(userRepository.get(validUsername)).thenReturn(User.of(validUsername, Token.of("ANOTHER_VALID_TOKEN", STATIC_NOW)));
        when(tokenRepository.get(validTokenValue)).thenReturn(Token.of(validTokenValue.value(), STATIC_NOW.plusDays(1)));

        //Act
        final LoginResult result = loginService.login(validUsername, validTokenValue);

        //Assert
        assertThat(result).isEqualTo(blocked());
    }

    @Test
    void shouldBlockWhenTokenHasExpired() {
        //Arrange
        final Username validUsername = Username.of(VALID_USERNAME);
        final TokenValue validTokenValue = TokenValue.of(VALID_TOKEN);

        when(userRepository.get(validUsername)).thenReturn(User.of(validUsername, Token.of(validTokenValue.value(), STATIC_NOW.minusDays(1))));
        when(tokenRepository.get(validTokenValue)).thenReturn(Token.of(validTokenValue.value(), STATIC_NOW.minusDays(1)));

        //Act
        final LoginResult result = loginService.login(validUsername, validTokenValue);

        //Assert
        assertThat(result).isEqualTo(blocked());
    }

    @Test
    void shouldReturnSuccessWhenUsernameAndTokenAreValid() {
        //Arrange
        final Username validUsername = Username.of(VALID_USERNAME);
        final TokenValue validTokenValue = TokenValue.of(VALID_TOKEN);
        final Token validToken = Token.of(validTokenValue.value(), STATIC_NOW.plusYears(9999));

        when(userRepository.get(validUsername)).thenReturn(User.of(validUsername, validToken));
        when(tokenRepository.get(validTokenValue)).thenReturn(validToken);

        //Act
        final LoginResult result = loginService.login(validUsername, validTokenValue);

        //Assert
        assertThat(result).isEqualTo(success());
    }

}
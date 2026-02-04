package com.sopra_steria.jens_berckmoes.service;

import com.sopra_steria.jens_berckmoes.TestConstants;
import com.sopra_steria.jens_berckmoes.domain.LoginResult;
import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.domain.exception.TokenNotFoundException;
import com.sopra_steria.jens_berckmoes.domain.exception.UserNotFoundException;
import com.sopra_steria.jens_berckmoes.domain.repository.TokenRepository;
import com.sopra_steria.jens_berckmoes.domain.repository.UserRepository;
import com.sopra_steria.jens_berckmoes.domain.valueobject.TokenValue;
import com.sopra_steria.jens_berckmoes.domain.valueobject.Username;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.ZoneId;

import static com.sopra_steria.jens_berckmoes.TestConstants.Tokens.*;
import static com.sopra_steria.jens_berckmoes.TestConstants.Users.*;
import static com.sopra_steria.jens_berckmoes.domain.LoginResult.blocked;
import static com.sopra_steria.jens_berckmoes.domain.LoginResult.success;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LoginServiceTest {
    private final UserRepository userRepository = mock(UserRepository.class);
    private final TokenRepository tokenRepository = mock(TokenRepository.class);
    private final Clock clock = Clock.fixed(
            TestConstants.TimeFixture.REFERENCE_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(),
            ZoneId.systemDefault()
    );
    private final LoginService loginService = new LoginService(userRepository, tokenRepository, clock);

    @Test
    void shouldBlockWhenUsernameIsInvalid() {
        //Arrange
        final Username invalidUsername = Username.of(INVALID_USERNAME);
        final TokenValue validTokenValue = TokenValue.of(VALID_RAW_TOKEN);

        when(userRepository.get(invalidUsername)).thenThrow(new UserNotFoundException());
        when(tokenRepository.get(validTokenValue)).thenReturn(VALID_TOKEN);

        //Act
        final LoginResult result = loginService.login(invalidUsername, validTokenValue);

        //Assert
        assertThat(result).isEqualTo(blocked());
    }

    @Test
    void shouldBlockWhenTokenIsInvalid() {
        //Arrange
        final Username validUsername = Username.of(VALID_USERNAME);
        final TokenValue invalidTokenValue = TokenValue.of(WRONG_RAW_TOKEN);

        when(userRepository.get(validUsername)).thenReturn(VALID_USER);
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
        final TokenValue validTokenValue = TokenValue.of(VALID_RAW_TOKEN);

        when(userRepository.get(validUsername)).thenReturn(User.of(validUsername, SECOND_VALID_TOKEN));
        when(tokenRepository.get(validTokenValue)).thenReturn(VALID_TOKEN);

        //Act
        final LoginResult result = loginService.login(validUsername, validTokenValue);

        //Assert
        assertThat(result).isEqualTo(blocked());
    }

    @Test
    void shouldBlockWhenTokenHasExpired() {
        //Arrange
        final Username validUsername = Username.of(VALID_USERNAME);
        final TokenValue validTokenValue = TokenValue.of(VALID_RAW_TOKEN);

        when(userRepository.get(validUsername)).thenReturn(User.of(validUsername, EXPIRED_TOKEN));
        when(tokenRepository.get(validTokenValue)).thenReturn(EXPIRED_TOKEN);

        //Act
        final LoginResult result = loginService.login(validUsername, validTokenValue);

        //Assert
        assertThat(result).isEqualTo(blocked());
    }

    @Test
    void shouldReturnSuccessWhenUsernameAndTokenAreValid() {
        //Arrange
        final Username validUsername = Username.of(VALID_USERNAME);
        final TokenValue validTokenValue = TokenValue.of(VALID_RAW_TOKEN);

        when(userRepository.get(validUsername)).thenReturn(VALID_USER);
        when(tokenRepository.get(validTokenValue)).thenReturn(VALID_TOKEN);

        //Act
        final LoginResult result = loginService.login(validUsername, validTokenValue);

        //Assert
        assertThat(result).isEqualTo(success());
    }

}
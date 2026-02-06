package com.sopra_steria.jens_berckmoes.service;

import com.sopra_steria.jens_berckmoes.TestConstants;
import com.sopra_steria.jens_berckmoes.domain.LoginResult;
import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.domain.exception.TokenHasExpiredException;
import com.sopra_steria.jens_berckmoes.domain.exception.TokenNotFoundException;
import com.sopra_steria.jens_berckmoes.domain.exception.UserNotFoundException;
import com.sopra_steria.jens_berckmoes.domain.repository.TokenRepository;
import com.sopra_steria.jens_berckmoes.domain.repository.UserRepository;
import com.sopra_steria.jens_berckmoes.domain.valueobject.TokenValue;
import com.sopra_steria.jens_berckmoes.domain.valueobject.Username;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Clock;
import java.time.ZoneId;
import java.util.stream.Stream;

import static com.sopra_steria.jens_berckmoes.TestConstants.Tokens.*;
import static com.sopra_steria.jens_berckmoes.TestConstants.Users.*;
import static com.sopra_steria.jens_berckmoes.domain.LoginResult.blocked;
import static com.sopra_steria.jens_berckmoes.domain.LoginResult.success;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("LoginService")
class LoginServiceTest {
    private final UserRepository userRepository = mock(UserRepository.class);
    private final TokenRepository tokenRepository = mock(TokenRepository.class);
    private final Clock clock = Clock.fixed(TestConstants.TimeFixture.REFERENCE_DATE.atStartOfDay(ZoneId.systemDefault())
            .toInstant(), ZoneId.systemDefault());
    private final LoginService loginService = new LoginService(userRepository, tokenRepository, clock);

    @ParameterizedTest
    @MethodSource("reasonsToBlockLoginParameters")
    @DisplayName("should block login when username or token are invalid, when token has expired or when token does not match the one associated with the user")
    void shouldDecideLogin(final String username, final String tokenValue, final LoginResult expectedResult, final int amountOfFindByUsernameCalls, final int amountOfFindByTokenValueCalls) {
        conditionallyMockFindByUsername(username, tokenValue);
        conditionallyMockFindByTokenValue(tokenValue);

        //Act
        final LoginResult result = loginService.login(Username.of(username), TokenValue.of(tokenValue));
        //Assert
        assertThat(result).isEqualTo(expectedResult);
        verify(userRepository, times(amountOfFindByUsernameCalls)).findByUsername(username);
        verify(tokenRepository, times(amountOfFindByTokenValueCalls)).findByTokenValue(tokenValue);
    }

    void conditionallyMockFindByUsername(final String username, final String tokenValue) {
        switch(username) {
            case "-" -> when(userRepository.findByUsername(username)).thenThrow(new UserNotFoundException());
            case USER_WITH_DIFFERENT_TOKEN_USERNAME ->
                    when(userRepository.findByUsername(username)).thenReturn(User.of(username, SECOND_VALID_TOKEN));
            case EXPIRED_USERNAME -> when(userRepository.findByUsername(username)).thenReturn(EXPIRED_USER);
            default -> when(userRepository.findByUsername(username)).thenReturn(User.of(username,
                    Token.of(tokenValue, TestConstants.TimeFixture.DATE_FAR_FUTURE)));
        }
    }

    void conditionallyMockFindByTokenValue(final String tokenValue) {
        switch(tokenValue) {
            case "-" -> when(tokenRepository.findByTokenValue(tokenValue)).thenThrow(new TokenNotFoundException());
            case EXPIRED_RAW_TOKEN ->
                    when(tokenRepository.findByTokenValue(tokenValue)).thenThrow(new TokenHasExpiredException());
            default -> when(tokenRepository.findByTokenValue(tokenValue)).thenReturn(Token.of(tokenValue,
                    TestConstants.TimeFixture.DATE_FAR_FUTURE));
        }
    }

    public static Stream<Arguments> reasonsToBlockLoginParameters() {
        return Stream.of(Arguments.of("-", "-", blocked(),1,0),
                Arguments.of("-", VALID_RAW_TOKEN, blocked(),1,0),
                Arguments.of(USER_WITH_DIFFERENT_TOKEN_USERNAME, VALID_RAW_TOKEN, blocked(),1,1),
                Arguments.of(EXPIRED_USERNAME, EXPIRED_RAW_TOKEN, blocked(),1,1),
                Arguments.of(VALID_USERNAME, "-", blocked(),1,1),
                Arguments.of(VALID_USERNAME, VALID_RAW_TOKEN, success(),1,1));
    }
}
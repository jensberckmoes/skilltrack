package com.sopra_steria.jens_berckmoes.service;

import com.sopra_steria.jens_berckmoes.TestConstants;
import com.sopra_steria.jens_berckmoes.domain.LoginResult;
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
class ValidateAssessmentAccessServiceTest {
    private final UserRepository userRepository = mock(UserRepository.class);
    private final TokenRepository tokenRepository = mock(TokenRepository.class);
    private final Clock clock = Clock.fixed(TestConstants.TimeFixture.TEST_TODAY.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
    private final ValidateAssessmentAccessService validateAssessmentAccessService = new ValidateAssessmentAccessService(userRepository, tokenRepository, clock);

    @ParameterizedTest
    @MethodSource("reasonsToBlockLoginParameters")
    @DisplayName("should block login when username or token are invalid, when token has expired or when token does not match the one associated with the user")
    void shouldDecideLogin(final String usernameRaw, final String token, final LoginResult expectedResult, final int amountOfFindByUsernameCalls, final int amountOfFindByTokenValueCalls) {
        final Username username = Username.of(usernameRaw);
        final TokenValue tokenValue = TokenValue.of(token);

        conditionallyMockFindByUsername(username);
        conditionallyMockFindByTokenValue(tokenValue);

        //Act
        final LoginResult result = validateAssessmentAccessService.login(username, tokenValue);
        //Assert
        assertThat(result).isEqualTo(expectedResult);
        verify(userRepository, times(amountOfFindByUsernameCalls)).findByUsername(username);
        verify(tokenRepository, times(amountOfFindByTokenValueCalls)).findByTokenValue(tokenValue);
    }

    void conditionallyMockFindByUsername(final Username username) {
        switch (username.value()) {
            case NON_EXISTING_USERNAME_RAW_STRING ->
                    when(userRepository.findByUsername(username)).thenThrow(new UserNotFoundException("User not found: " + username));
            case "geert@example.com" -> when(userRepository.findByUsername(username)).thenReturn(GEERT);
            case "bob@example.com" -> when(userRepository.findByUsername(username)).thenReturn(BOB);
            default -> when(userRepository.findByUsername(username)).thenReturn(ALICE);
        }
    }

    void conditionallyMockFindByTokenValue(final TokenValue tokenValue) {
        switch (tokenValue.value()) {
            case NON_EXISTING_TOKEN_RAW_STRING ->
                    when(tokenRepository.findByTokenValue(tokenValue)).thenThrow(new TokenNotFoundException("Token not found: " + NON_EXISTING_TOKEN_RAW_STRING));
            case "Z9y8X7w6V5u4T3s2R1q" ->
                    when(tokenRepository.findByTokenValue(tokenValue)).thenThrow(new TokenHasExpiredException());
            default -> when(tokenRepository.findByTokenValue(tokenValue)).thenReturn(ALICE_TOKEN);
        }
    }

    public static Stream<Arguments> reasonsToBlockLoginParameters() {
        return Stream.of(Arguments.of(NON_EXISTING_USERNAME_RAW_STRING, NON_EXISTING_TOKEN_RAW_STRING, blocked(), 1, 0),
                Arguments.of(NON_EXISTING_USERNAME_RAW_STRING, ALICE_TOKEN.token(), blocked(), 1, 0),
                Arguments.of(GEERT.username(), ALICE_TOKEN.token(), blocked(), 1, 1),
                Arguments.of(BOB.username(), BOB_TOKEN.token(), blocked(), 1, 1),
                Arguments.of(ALICE.username(), NON_EXISTING_TOKEN_RAW_STRING, blocked(), 1, 1),
                Arguments.of(ALICE.username(), ALICE_TOKEN.token(), success(), 1, 1));
    }
}
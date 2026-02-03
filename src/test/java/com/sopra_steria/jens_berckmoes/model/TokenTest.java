package com.sopra_steria.jens_berckmoes.model;

import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.domain.exception.TokenRawValueNullOrBlankException;
import com.sopra_steria.jens_berckmoes.domain.exception.TokenValidUntilNullException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static com.sopra_steria.jens_berckmoes.TestConstants.Tokens.STATIC_NOW;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class TokenTest {
    @ParameterizedTest
    @MethodSource("invalidTokens")
    void shouldThrowIfInputIsInvalid(final String token, final LocalDate expirationDate) {
        assertThatThrownBy(() -> Token.of(token, expirationDate))
                .isInstanceOf(TokenRawValueNullOrBlankException.class);
    }

    @Test
    void shouldThrowIfValidUntilIsInvalid() {
        assertThatThrownBy(() -> Token.of("t", null))
                .isInstanceOf(TokenValidUntilNullException.class);
    }

    @Test
    void shouldGiveCorrectDateOneWeekInFuture() {
        final Token givenToken = Token.weeklyToken("t");
        assertThat(givenToken.expirationDate()).isEqualTo(LocalDate.now().plusDays(7));
    }

    @ParameterizedTest
    @MethodSource("invalidWeeklyTokens")
    void shouldThrowIfInputIsInvalidForWeekly(final String token) {
        assertThatThrownBy(() -> Token.weeklyToken(token))
                .isInstanceOf(TokenRawValueNullOrBlankException.class);
    }

    public static Stream<Arguments> invalidTokens() {
        final LocalDate validTokenOneDay = STATIC_NOW.plusDays(1);
        return Stream.of(
                Arguments.of(null, validTokenOneDay),
                Arguments.of("", validTokenOneDay),
                Arguments.of("  ", validTokenOneDay));
    }

    public static Stream<Arguments> invalidWeeklyTokens() {
        final String tokenValue = "value";
        return Stream.of(
                Arguments.of(null, tokenValue),
                Arguments.of("", tokenValue),
                Arguments.of("  ", tokenValue));
    }

    @ParameterizedTest
    @MethodSource("datesProvider")
    void shouldDetermineExpirationCorrectly(final LocalDate referenceDate, final boolean expectedExpired) {
        final Token givenToken = Token.of("t", STATIC_NOW);
        assertThat(givenToken.isExpiredAt(referenceDate)).isEqualTo(expectedExpired);
    }

    public static Stream<Arguments> datesProvider() {
        return Stream.of(
                Arguments.of(STATIC_NOW.plusDays(1), true),
                Arguments.of(STATIC_NOW, false),
                Arguments.of(STATIC_NOW.minusDays(1), false));
    }
}

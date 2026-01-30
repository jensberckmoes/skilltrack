package com.sopra_steria.jens_berckmoes.model;

import com.sopra_steria.jens_berckmoes.exception.TokenRawValueNullOrBlankException;
import com.sopra_steria.jens_berckmoes.exception.TokenValidUntilNullException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class TokenTest {
    private static final LocalDateTime STATIC_NOW = LocalDateTime.of(2026, 1, 30, 12, 15, 0);

    @ParameterizedTest
    @MethodSource("invalidTokens")
    void shouldThrowIfInputIsInvalid(final String token, final LocalDateTime validUntil) {
        assertThatThrownBy(() -> Token.of(token, validUntil))
                .isInstanceOf(TokenRawValueNullOrBlankException.class);
    }

    @Test
    void shouldThrowIfValidUntilIsInvalid() {
        assertThatThrownBy(() -> Token.of("t", null))
                .isInstanceOf(TokenValidUntilNullException.class);
    }


    @ParameterizedTest
    @MethodSource("invalidWeeklyTokens")
    void shouldThrowIfInputIsInvalidForWeekly(final String token) {
        assertThatThrownBy(() -> Token.weeklyToken(token))
                .isInstanceOf(TokenRawValueNullOrBlankException.class);
    }

    public static Stream<Arguments> invalidTokens() {
        final LocalDateTime validTokenOneDay = STATIC_NOW.plusDays(1);
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
    @MethodSource("timestampsProvider")
    void shouldDetermineExpirationCorrectly(final LocalDateTime referenceTime, final boolean expectedExpired) {
        final Token givenToken = Token.of("t", STATIC_NOW);
        assertThat(givenToken.isExpiredAt(referenceTime)).isEqualTo(expectedExpired);
    }

    public static Stream<Arguments> timestampsProvider() {
        return Stream.of(
                Arguments.of(STATIC_NOW.plusHours(1), true),
                Arguments.of(STATIC_NOW, false),
                Arguments.of(STATIC_NOW.minusHours(1), false));
    }
}

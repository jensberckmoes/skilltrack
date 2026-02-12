package com.sopra_steria.jens_berckmoes.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Domain Token")
class TokenTest {

    @ParameterizedTest
    @MethodSource("tokensWithExpirationDateProvider")
    @DisplayName("should determine whether a token has expired correctly ")
    void shouldDetermineExpirationCorrectly(final Token token, final boolean expectedExpired) {
        final boolean actual = token.hasExpired();

        assertThat(actual).isEqualTo(expectedExpired);
    }

    public static Stream<Arguments> tokensWithExpirationDateProvider() {
        final LocalDate fixedToday = LocalDate.of(2026, 2, 12);
        return Stream.of(
                Arguments.of(Token.of("x", fixedToday), false),
                Arguments.of(Token.of("x", fixedToday.minusDays(1)), true),
                Arguments.of(Token.of("x", fixedToday.minusDays(5)), true),
                Arguments.of(Token.of("x", fixedToday.plusDays(3)), false),
                Arguments.of(Token.of("x", fixedToday), false));
    }

    @ParameterizedTest
    @MethodSource("referenceDatesProvider")
    @DisplayName("should determine whether a token has expired correctly using a reference date")
    void shouldDetermineExpirationCorrectlyUsingReferenceDate(final Token token, final LocalDate referenceDate, final boolean expectedExpired) {
        final boolean actual = token.hasExpired(referenceDate);

        assertThat(actual).isEqualTo(expectedExpired);
    }

    public static Stream<Arguments> referenceDatesProvider() {
        final LocalDate fixedToday = LocalDate.of(2026, 2, 12);
        return Stream.of(
                Arguments.of(Token.of("x", fixedToday), fixedToday, false),
                Arguments.of(Token.of("x", fixedToday.minusDays(1)), fixedToday, true),
                Arguments.of(Token.of("x", fixedToday.minusDays(5)), fixedToday, true),
                Arguments.of(Token.of("x", fixedToday.plusDays(3)), fixedToday, false),
                Arguments.of(Token.of("x", fixedToday), fixedToday, false));
    }

}

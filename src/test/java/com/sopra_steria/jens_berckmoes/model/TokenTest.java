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

import static com.sopra_steria.jens_berckmoes.TestConstants.TimeFixture.*;
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

    public static Stream<Arguments> invalidTokens() {
        final LocalDate validTokenOneDay = DATE_TOMORROW;
        return Stream.of(
                Arguments.of(null, validTokenOneDay),
                Arguments.of("", validTokenOneDay),
                Arguments.of("  ", validTokenOneDay));
    }

    @ParameterizedTest
    @MethodSource("datesProvider")
    void shouldDetermineExpirationCorrectly(final LocalDate referenceDate, final boolean expectedExpired) {
        final Token givenToken = Token.of("t", REFERENCE_DATE);
        assertThat(givenToken.isExpiredAt(referenceDate)).isEqualTo(expectedExpired);
    }

    public static Stream<Arguments> datesProvider() {
        return Stream.of(
                Arguments.of(DATE_TOMORROW, true),
                Arguments.of(REFERENCE_DATE, false),
                Arguments.of(DATE_YESTERDAY, false));
    }
}

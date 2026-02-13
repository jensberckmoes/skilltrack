package com.sopra_steria.jens_berckmoes.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.stream.Stream;

import static com.sopra_steria.jens_berckmoes.TestConstants.TimeFixture.FIXED_CLOCK;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Domain Token")
class TokenTest {

    @ParameterizedTest
    @MethodSource("referenceDatesProvider")
    @DisplayName("should determine whether a token has expired correctly using a reference date")
    void shouldDetermineExpirationCorrectlyUsingReferenceDate(final LocalDate referenceDate, final boolean expectedExpired) {
        final Clock expiredClockDate = Clock.fixed(referenceDate.atStartOfDay(ZoneOffset.UTC).toInstant(), ZoneOffset.UTC);
        final boolean actual = Token.of("x", LocalDate.now(FIXED_CLOCK)).hasExpired(expiredClockDate);

        assertThat(actual).isEqualTo(expectedExpired);
    }

    public static Stream<Arguments> referenceDatesProvider() {
       final LocalDate fixedToday = LocalDate.now(FIXED_CLOCK);
        return Stream.of(
                Arguments.of( fixedToday, false),
                Arguments.of(  fixedToday.plusDays(1), true),
                Arguments.of(  fixedToday.plusDays(5), true),
                Arguments.of(  fixedToday.minusDays(3), false),
                Arguments.of(  fixedToday, false));
    }

}

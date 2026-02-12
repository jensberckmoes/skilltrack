package com.sopra_steria.jens_berckmoes.domain.valueobject;

import com.sopra_steria.jens_berckmoes.bdd.fakes.StepResult;
import com.sopra_steria.jens_berckmoes.domain.exception.UsernameRawValueNullOrBlankException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.sopra_steria.jens_berckmoes.TestConstants.BLANK;
import static com.sopra_steria.jens_berckmoes.TestConstants.Users.ALICE_USERNAME_RAW_STRING;
import static org.apache.logging.log4j.util.Strings.EMPTY;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Username")
class UsernameTest {

    @Test
    @DisplayName("should create a valid username when given a valid raw username")
    void shouldCreateValidUsername() {
        final StepResult<Username> result = StepResult.callController(() -> Username.of(ALICE_USERNAME_RAW_STRING));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.body().value()).isEqualTo(ALICE_USERNAME_RAW_STRING);
    }

    @ParameterizedTest
    @MethodSource("invalidRawUsernamesValues")
    @DisplayName("should throw UsernameRawValueNullOrBlankException when given an invalid raw username")
    void shouldThrowIfInputIsInvalid(final String rawUsername, final String message) {
        final StepResult<Username> result = StepResult.callController(() -> Username.of(rawUsername));

        assertThat(result.isFailure()).isTrue();
        assertThat(result.exception()).isInstanceOf(UsernameRawValueNullOrBlankException.class);
        assertThat(result.exception().getMessage()).isEqualTo(message);
    }

    public static Stream<Arguments> invalidRawUsernamesValues() {
        return Stream.of(Arguments.of(null, "Username can not be null"),
                Arguments.of(EMPTY, "Username can not be blank"),
                Arguments.of(BLANK, "Username can not be blank"));
    }

}

package com.sopra_steria.jens_berckmoes.domain.valueobject;

import com.sopra_steria.jens_berckmoes.domain.exception.UsernameRawValueNullOrBlankException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.sopra_steria.jens_berckmoes.TestConstants.BLANK;
import static com.sopra_steria.jens_berckmoes.TestConstants.Users.VALID_USERNAME;
import static org.apache.logging.log4j.util.Strings.EMPTY;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("Username")
class UsernameTest {

    @Test
    @DisplayName("should create a valid username when given a valid raw username")
    void shouldCreateValidUsername() {
        assertThat(Username.of(VALID_USERNAME).value()).isEqualTo(VALID_USERNAME);
    }

    @ParameterizedTest
    @MethodSource("invalidRawUsernamesValues")
    @DisplayName("should throw UsernameRawValueNullOrBlankException when given an invalid raw username")
    void shouldThrowIfInputIsInvalid(final String rawUsername) {
        assertThatThrownBy(() -> Username.of(rawUsername)).isInstanceOf(UsernameRawValueNullOrBlankException.class);
    }

    public static Stream<Arguments> invalidRawUsernamesValues() {
        return Stream.of(Arguments.of((String) null),//cast for confused varargs
                Arguments.of(EMPTY), Arguments.of(BLANK));
    }

}

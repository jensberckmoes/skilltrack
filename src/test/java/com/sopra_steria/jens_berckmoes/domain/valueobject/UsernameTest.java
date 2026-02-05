package com.sopra_steria.jens_berckmoes.domain.valueobject;

import com.sopra_steria.jens_berckmoes.domain.exception.UsernameRawValueNullOrBlankException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class UsernameTest {

    @Test
    @DisplayName("Username should create a valid username when given a valid raw username")
    void shouldCreateValidUsername() {
        final Username username = Username.of("jane.doe@example.com");
        assertThat(username.value()).isEqualTo("jane.doe@example.com");
    }

    @ParameterizedTest
    @MethodSource("invalidRawUsernamesValues")
    @DisplayName("Username should throw UsernameRawValueNullOrBlankException when given an invalid raw username")
    void shouldThrowIfInputIsInvalid(final String rawUsername) {
        assertThatThrownBy(() -> Username.of(rawUsername))
                .isInstanceOf(UsernameRawValueNullOrBlankException.class);
    }

    public static Stream<Arguments> invalidRawUsernamesValues() {
        return Stream.of(
                Arguments.of((String) null),//cast for confused varargs
                Arguments.of(""),
                Arguments.of("  "));
    }

}

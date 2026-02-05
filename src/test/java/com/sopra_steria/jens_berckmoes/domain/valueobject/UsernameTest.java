package com.sopra_steria.jens_berckmoes.domain.valueobject;

import com.sopra_steria.jens_berckmoes.domain.exception.UsernameRawValueNullOrBlankException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class UsernameTest {

    @Test
    void shouldCreateValidUsername() {
        final Username username = Username.of("jane.doe@example.com");
        assertThat(username.value()).isEqualTo("jane.doe@example.com");
    }

    @ParameterizedTest
    @MethodSource("invalidRawUsernamesValues")
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

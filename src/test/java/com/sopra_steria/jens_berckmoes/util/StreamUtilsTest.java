package com.sopra_steria.jens_berckmoes.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.sopra_steria.jens_berckmoes.TestConstants.BLANK;
import static org.apache.logging.log4j.util.Strings.EMPTY;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("StreamUtils")
class StreamUtilsTest {

    @ParameterizedTest
    @MethodSource("reasonsToReturnEmptyStreamParameters")
    @DisplayName("should be able to stream values or return an empty stream")
    void shouldHandleEmptyListAndReturnEmptyStream(final Iterable<Object> iterable, final Stream<Object> objectStream) {
        //Act
        final Stream<Object> stream = StreamUtils.toStream(iterable);

        //Assert
        assertThat(stream.collect(Collectors.toSet())).isEqualTo(objectStream.collect(Collectors.toSet()));
    }

    public static Stream<Arguments> reasonsToReturnEmptyStreamParameters() {
        return Stream.of(Arguments.of(null, Stream.empty()),
                Arguments.of(List.of(), Stream.empty()),
                Arguments.of(Collections.singletonList(null), Stream.empty()),
                Arguments.of(List.of(EMPTY), Stream.empty()),
                Arguments.of(List.of(BLANK), Stream.empty()),
                Arguments.of(List.of("a"), Stream.of("a")),
                Arguments.of(List.of("a", "b"), Stream.of("a", "b")),
                Arguments.of(List.of("a", "b", "c"), Stream.of("a", "b", "c")),
                Arguments.of(List.of(1, 2, 4), Stream.of(1, 2, 4)),
                Arguments.of(List.of(BigDecimal.ONE, BigDecimal.TEN), Stream.of(BigDecimal.ONE, BigDecimal.TEN)),
                Arguments.of(Set.of(BigDecimal.ONE, BigDecimal.TEN), Stream.of(BigDecimal.ONE, BigDecimal.TEN)));
    }


   /* public static <T> Stream<T> toStream(final Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    public static <T> List<T> toList(final Iterable<T> iterable) {
        return toStream(iterable).toList();
    }
*/
}
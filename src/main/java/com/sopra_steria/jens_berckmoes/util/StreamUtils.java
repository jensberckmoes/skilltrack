package com.sopra_steria.jens_berckmoes.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StreamUtils {

    public static <T> Stream<T> toStream(final Iterable<T> iterable) {
        if(iterable == null) {
            return Stream.empty();
        }
        return StreamSupport.stream(iterable.spliterator(), false).filter(StreamUtils::isNotNullOrNotBlank);
    }

    private static <T> boolean isNotNullOrNotBlank(final T next) {
        if(next == null) {
            return false;
        }
        if(next instanceof final String string) {
            return !string.isBlank();
        }
        return true;
    }

    public static <T> List<T> toList(final Iterable<T> iterable) {
        return toStream(iterable).toList();
    }

}
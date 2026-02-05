package com.sopra_steria.jens_berckmoes.infra.mapping;

import com.sopra_steria.jens_berckmoes.TestConstants;
import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.infra.entity.TokenEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class TokenMapperTest {
    private final LocalDate referenceDate = TestConstants.TimeFixture.REFERENCE_DATE;

    @Test
    void shouldMapUserEntityToUserCorrectly() {
        final TokenEntity tokenEntityToMap = TokenEntity.builder()
                .value("hashed-token")
                .expirationDate(referenceDate)
                .build();
        final Token assertedToken = Token.of("hashed-token", referenceDate);

        final Token mappedResult = TokenMapper.mapToDomain(tokenEntityToMap);

        Assertions.assertThat(mappedResult).isEqualTo(assertedToken);
    }

    @Test
    void shouldMapUserToUserEntityCorrectly() {
        final TokenEntity assertedTokenEntity = TokenEntity.builder()
                .value("hashed-token")
                .expirationDate(referenceDate)
                .build();
        final Token tokenToMap = Token.of("hashed-token", referenceDate);

        final TokenEntity mappedResult = TokenMapper.mapToInfra(tokenToMap);

        Assertions.assertThat(mappedResult).isEqualTo(assertedTokenEntity);
    }
}

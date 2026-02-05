package com.sopra_steria.jens_berckmoes.bdd.fakes;

import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.domain.exception.TokenNotFoundException;
import com.sopra_steria.jens_berckmoes.domain.repository.TokenRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
@Profile("test")
public record InMemoryTokenRepository(Map<String, Token> tokens) implements TokenRepository {

    @Override
    public Token findByTokenValue(final String tokenValue) throws TokenNotFoundException {
        return Optional.ofNullable(tokens.get(tokenValue))
                .orElseThrow(TokenNotFoundException::new);
    }
}

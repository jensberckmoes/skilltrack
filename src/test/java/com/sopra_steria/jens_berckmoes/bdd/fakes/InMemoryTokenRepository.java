package com.sopra_steria.jens_berckmoes.bdd.fakes;

import com.sopra_steria.jens_berckmoes.exception.TokenNotFoundException;
import com.sopra_steria.jens_berckmoes.model.Token;
import com.sopra_steria.jens_berckmoes.model.TokenValue;
import com.sopra_steria.jens_berckmoes.repository.TokenRepository;

import java.time.LocalDateTime;
import java.util.Map;

import static com.sopra_steria.jens_berckmoes.TestConstants.Tokens.EXPIRED_TOKEN;
import static com.sopra_steria.jens_berckmoes.TestConstants.Tokens.VALID_TOKEN;

public class InMemoryTokenRepository implements TokenRepository {
    private final Map<TokenValue, Token> tokens;

    public InMemoryTokenRepository(final LocalDateTime dateTime) {
        this.tokens = constructTokens(dateTime);
    }

    private Map<TokenValue, Token> constructTokens(final LocalDateTime dateTime) {
        return Map.ofEntries(
                Map.entry(TokenValue.of(VALID_TOKEN), Token.of(TokenValue.of(VALID_TOKEN).value(), dateTime.plusDays(1))),
                Map.entry(TokenValue.of(EXPIRED_TOKEN), Token.of(TokenValue.of(EXPIRED_TOKEN).value(), dateTime.minusYears(10))));
    }

    @Override
    public Map<TokenValue, Token> getTokens() {
        return tokens;
    }

    @Override
    public Token get(final TokenValue tokenValue) {
        final Token token = tokens.get(tokenValue);
        if (token == null) {
            throw new TokenNotFoundException();
        }
        return token;
    }
}

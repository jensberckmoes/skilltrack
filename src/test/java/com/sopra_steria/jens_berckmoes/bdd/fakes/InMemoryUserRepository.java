package com.sopra_steria.jens_berckmoes.bdd.fakes;

import com.sopra_steria.jens_berckmoes.exception.UserNotFoundException;
import com.sopra_steria.jens_berckmoes.model.Token;
import com.sopra_steria.jens_berckmoes.model.TokenValue;
import com.sopra_steria.jens_berckmoes.model.User;
import com.sopra_steria.jens_berckmoes.model.Username;
import com.sopra_steria.jens_berckmoes.repository.UserRepository;

import java.util.Map;

import static com.sopra_steria.jens_berckmoes.TestConstants.Tokens.EXPIRED_TOKEN;
import static com.sopra_steria.jens_berckmoes.TestConstants.Tokens.VALID_TOKEN;
import static com.sopra_steria.jens_berckmoes.TestConstants.Users.EXPIRED_USERNAME;
import static com.sopra_steria.jens_berckmoes.TestConstants.Users.VALID_USERNAME;

public class InMemoryUserRepository implements UserRepository {
    private final Map<Username, User> users;

    public InMemoryUserRepository(final Map<TokenValue, Token> tokens) {
        this.users = Map.ofEntries(
                Map.entry(Username.of(VALID_USERNAME), User.of(Username.of(VALID_USERNAME),tokens.get(TokenValue.of(VALID_TOKEN)))),
                Map.entry(Username.of(EXPIRED_USERNAME), User.of(Username.of(EXPIRED_USERNAME),tokens.get(TokenValue.of(EXPIRED_TOKEN))))
        );
    }

    @Override
    public User get(final Username username) throws UserNotFoundException {
        final User user = users.get(username);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }

}

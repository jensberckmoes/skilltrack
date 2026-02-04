package com.sopra_steria.jens_berckmoes.bdd.config;

import com.sopra_steria.jens_berckmoes.TestConstants;
import com.sopra_steria.jens_berckmoes.bdd.fakes.InMemoryTokenRepository;
import com.sopra_steria.jens_berckmoes.bdd.fakes.InMemoryUserRepository;
import com.sopra_steria.jens_berckmoes.domain.Token;
import com.sopra_steria.jens_berckmoes.domain.valueobject.TokenValue;
import com.sopra_steria.jens_berckmoes.domain.User;
import com.sopra_steria.jens_berckmoes.domain.valueobject.Username;
import com.sopra_steria.jens_berckmoes.domain.repository.TokenRepository;
import com.sopra_steria.jens_berckmoes.domain.repository.UserRepository;
import com.sopra_steria.jens_berckmoes.service.LoginService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class TestConfig {

    @Bean
    public LoginService loginService() {
        return new LoginService(userRepository(), tokenRepository());
    }
    @Bean
    public UserRepository userRepository() {
        return new InMemoryUserRepository(users());
    }

    @Bean
    public TokenRepository tokenRepository() {
        return new InMemoryTokenRepository(tokens());
    }
    @Bean
    public Map<TokenValue, Token> tokens(){
        return TestConstants.Tokens.TEST_TOKENS;
    }

    @Bean
    public Map<Username, User> users(){
        return TestConstants.Users.TEST_USERS;
    }
}

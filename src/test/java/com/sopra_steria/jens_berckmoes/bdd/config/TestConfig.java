package com.sopra_steria.jens_berckmoes.bdd.config;

import com.sopra_steria.jens_berckmoes.TestConstants;
import com.sopra_steria.jens_berckmoes.bdd.fakes.InMemoryTokenRepository;
import com.sopra_steria.jens_berckmoes.bdd.fakes.InMemoryUserRepository;
import com.sopra_steria.jens_berckmoes.domain.repository.TokenRepository;
import com.sopra_steria.jens_berckmoes.domain.repository.UserRepository;
import com.sopra_steria.jens_berckmoes.service.LoginService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @Bean
    public LoginService loginService() {
        return new LoginService(userRepository(), tokenRepository());
    }

    @Bean
    public UserRepository userRepository() {
        return new InMemoryUserRepository(TestConstants.Users.TEST_USERS);
    }

    @Bean
    public TokenRepository tokenRepository() {
        return new InMemoryTokenRepository(TestConstants.Tokens.TEST_TOKENS);
    }

}

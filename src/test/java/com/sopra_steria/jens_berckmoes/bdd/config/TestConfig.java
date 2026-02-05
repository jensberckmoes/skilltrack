package com.sopra_steria.jens_berckmoes.bdd.config;

import com.sopra_steria.jens_berckmoes.TestConstants;
import com.sopra_steria.jens_berckmoes.bdd.fakes.InMemoryTokenRepository;
import com.sopra_steria.jens_berckmoes.bdd.fakes.InMemoryUserRepository;
import com.sopra_steria.jens_berckmoes.domain.repository.TokenRepository;
import com.sopra_steria.jens_berckmoes.domain.repository.UserRepository;
import com.sopra_steria.jens_berckmoes.service.LoginService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Clock;
import java.time.ZoneId;

import static com.sopra_steria.jens_berckmoes.TestConstants.TimeFixture.REFERENCE_DATE;

@Configuration
@Profile("test")
public class TestConfig {

    @Bean
    public LoginService loginService() {
        return new LoginService(userRepository(), tokenRepository(), clock());
    }

    @Bean
    public UserRepository userRepository() {
        return new InMemoryUserRepository(TestConstants.Users.TEST_USERS);
    }

    @Bean
    public TokenRepository tokenRepository() {
        return new InMemoryTokenRepository(TestConstants.Tokens.TEST_TOKENS);
    }

    @Bean
    public Clock clock() {
        return Clock.fixed(
                REFERENCE_DATE.atStartOfDay(ZoneId.systemDefault()).toInstant(),
                ZoneId.systemDefault()
        );
    }

}

package com.sopra_steria.jens_berckmoes.bdd.config;

import com.sopra_steria.jens_berckmoes.bdd.fakes.InMemoryTokenRepository;
import com.sopra_steria.jens_berckmoes.bdd.fakes.InMemoryUserRepository;
import com.sopra_steria.jens_berckmoes.controller.UserController;
import com.sopra_steria.jens_berckmoes.domain.repository.TokenRepository;
import com.sopra_steria.jens_berckmoes.domain.repository.UserRepository;
import com.sopra_steria.jens_berckmoes.service.ValidateAssessmentAccessService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.time.Clock;
import java.time.ZoneId;
import java.util.HashMap;

import static com.sopra_steria.jens_berckmoes.TestConstants.TimeFixture.TEST_TODAY;
import static com.sopra_steria.jens_berckmoes.TestConstants.Tokens.BDD_TOKENS_WITH_REALISTIC_VALUES;
import static com.sopra_steria.jens_berckmoes.TestConstants.Users.BDD_USERS_WITH_REALISTIC_VALUES;

@Configuration
@Profile("test")
public class TestConfig {

    @Bean
    public ValidateAssessmentAccessService loginService() {
        return new ValidateAssessmentAccessService(userRepository(), tokenRepository(), clock());
    }

    @Bean
    public UserRepository userRepository() {
        return new InMemoryUserRepository(new HashMap<>(BDD_USERS_WITH_REALISTIC_VALUES));
    }

    @Bean
    public TokenRepository tokenRepository() {
        return new InMemoryTokenRepository(new HashMap<>(BDD_TOKENS_WITH_REALISTIC_VALUES));
    }

    @Bean
    public Clock clock() {
        return Clock.fixed(TEST_TODAY.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
    }

    @Bean
    public UserController userController() {
        return new UserController(userRepository(), tokenRepository());
    }

    @Bean
    public RestTestClient restClient() {
        return RestTestClient.bindToController(userController()).build();
    }
}

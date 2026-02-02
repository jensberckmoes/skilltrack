package com.sopra_steria.jens_berckmoes.config;

import com.sopra_steria.jens_berckmoes.repository.DatabaseTokenRepository;
import com.sopra_steria.jens_berckmoes.repository.DatabaseUserRepository;
import com.sopra_steria.jens_berckmoes.repository.TokenRepository;
import com.sopra_steria.jens_berckmoes.repository.UserRepository;
import com.sopra_steria.jens_berckmoes.service.LoginService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class ApplicationConfig {

    @Bean
    public LoginService loginService() {
        return new LoginService(userRepository(), tokenRepository());
    }

    @Bean
    public UserRepository userRepository() {
        return new DatabaseUserRepository();
    }

    @Bean
    public TokenRepository tokenRepository() {
        return new DatabaseTokenRepository();
    }
}

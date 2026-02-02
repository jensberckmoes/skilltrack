package com.sopra_steria.jens_berckmoes.bdd.config;

import com.sopra_steria.jens_berckmoes.TestConstants;
import com.sopra_steria.jens_berckmoes.model.Token;
import com.sopra_steria.jens_berckmoes.model.TokenValue;
import com.sopra_steria.jens_berckmoes.model.User;
import com.sopra_steria.jens_berckmoes.model.Username;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class TestConfig {

    @Bean
    public Map<TokenValue, Token> tokens(){
        return TestConstants.Tokens.TEST_TOKENS;
    }

    @Bean
    public Map<Username, User> users(){
        return TestConstants.Users.TEST_USERS;
    }
}

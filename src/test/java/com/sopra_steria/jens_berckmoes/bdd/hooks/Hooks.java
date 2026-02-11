package com.sopra_steria.jens_berckmoes.bdd.hooks;

import com.sopra_steria.jens_berckmoes.bdd.fakes.InMemoryUserRepository;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.springframework.beans.factory.annotation.Autowired;

import static com.sopra_steria.jens_berckmoes.TestConstants.UserEntities.USER_ENTITIES_AS_SET;

public class Hooks {

    @Autowired
    private InMemoryUserRepository userRepository;

    @Before("not @NoUsers")
    public void setup() {
        userRepository.deleteAll();
        userRepository.saveAll(USER_ENTITIES_AS_SET);
    }

    @After
    public void breakdown() {
        userRepository.deleteAll();
    }
}

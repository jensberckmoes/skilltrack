package com.sopra_steria.jens_berckmoes.bdd.hooks;

import com.sopra_steria.jens_berckmoes.bdd.fakes.InMemoryUserRepository;
import com.sopra_steria.jens_berckmoes.infra.entity.UserEntity;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static com.sopra_steria.jens_berckmoes.TestConstants.Users.BDD_USERS;
import static com.sopra_steria.jens_berckmoes.infra.mapping.UserMapper.mapToInfra;

public class Hooks {

    @Autowired private InMemoryUserRepository userRepository;

    @Before("not @NoUsers")
    public void setup() {
        userRepository.deleteAll();
        final Set<UserEntity> userEntities = mapToInfra(BDD_USERS.values());
        userRepository.saveAll(userEntities);
    }

    @After
    public void breakdown() {
        userRepository.deleteAll();
    }
}

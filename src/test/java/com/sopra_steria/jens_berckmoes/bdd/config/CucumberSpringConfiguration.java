package com.sopra_steria.jens_berckmoes.bdd.config;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@CucumberContextConfiguration
@SpringBootTest(classes = {TestConfig.class})
@ActiveProfiles("test")
public class CucumberSpringConfiguration {

}
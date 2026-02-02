package com.sopra_steria.jens_berckmoes.bdd.config;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@CucumberContextConfiguration
@SpringBootTest
@Import(TestConfig.class)
@ActiveProfiles("test")
public class CucumberSpringConfiguration {

}
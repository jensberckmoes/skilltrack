package com.sopra_steria.jens_berckmoes.integration.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.time.Clock;

import static com.sopra_steria.jens_berckmoes.TestConstants.TimeFixture.FIXED_CLOCK;

@TestConfiguration
@Profile("integration")
public class IntegrationConfig {
    @Bean
    @Primary
    public Clock fixedClock() {
        return FIXED_CLOCK;
    }
}

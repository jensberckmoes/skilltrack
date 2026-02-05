package com.sopra_steria.jens_berckmoes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;

@Configuration
public class AppConfig {

    @Bean
    public Clock clock() {
        return Clock.fixed(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
    }
}

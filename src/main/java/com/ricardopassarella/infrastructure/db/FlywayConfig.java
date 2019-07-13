package com.ricardopassarella.infrastructure.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FlywayConfig {

    @Autowired
    private final DataSource dataSource;

    @Bean
    @Profile(value = "local")
    public Flyway flyway() {
        // TODO change location for classpath if profile != local

        Flyway flyway = Flyway.configure()
                              .dataSource(dataSource)
                              .baselineOnMigrate(true)
                              .locations(
                                      "filesystem:src/main/resources/db/migration/common/",
                                      "filesystem:src/main/resources/db/migration/localdata/")
                              .load();

        log.info("Running flyway migration");
        flyway.migrate();
        return flyway;
    }
}

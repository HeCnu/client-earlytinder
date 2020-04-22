package ru.liga.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.liga.service.AppService;

@Configuration
public class AppConfig {

    @Bean
    public AppService appService() {
        return new AppService();
    }
}

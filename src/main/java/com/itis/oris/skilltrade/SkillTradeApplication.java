package com.itis.oris.skilltrade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableCaching
@EnableJpaRepositories(basePackages = "com.itis.oris.skilltrade.repository")
public class SkillTradeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkillTradeApplication.class, args);
    }
}

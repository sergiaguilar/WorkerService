package com.everis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories
@ComponentScan({"com.everis.*"})
@EntityScan({"com.everis.*"})
public class WorkerApp {



    public static void main(String[] args) throws Exception {
        SpringApplication.run(WorkerApp.class, args);
    }


}
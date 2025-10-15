package com.andgivemarketing.problemmaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ProblemMakerApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext run = SpringApplication.run(ProblemMakerApplication.class, args);

    }

}

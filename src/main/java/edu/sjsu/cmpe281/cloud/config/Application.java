package edu.sjsu.cmpe281.cloud.config;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Nakul Sharma
 *         Class to configure application context
 *         SpringBootApplication doesn't support base package feature hence below 3 annotations has been used
 */
@Configuration
@ComponentScan(basePackages = "edu.sjsu.cmpe281.cloud")
@EnableAutoConfiguration
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

package edu.sjsu.cmpe281.cloud.config;


import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Nakul Sharma
 *         Class to configure application context
 *         SpringBootApplication doesn't support base package feature hence below 3 annotations has been used
 */
@Configuration
@ComponentScan(basePackages = "edu.sjsu.cmpe281.cloud")
@EnableAutoConfiguration
@EnableScheduling
public class Application {

    public static void main(String[] args) {

        FetchScheduler fetchScheduler=new FetchScheduler();
        fetchScheduler.updateDatabasePeriodically();

    }
}

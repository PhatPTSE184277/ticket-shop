package com.ticketShop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
public class StartApplication {
    public static void main(String[] args) {
        //
//        SpringApplication app = new SpringApplication(StartApplication.class);
//        app.setApplicationStartup(new BufferingApplicationStartup(2048));
//        app.run(args);
        SpringApplication.run(StartApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
package com.example.stage7;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class Stage7Application {

    public static void main(String[] args) {
        SpringApplication.run(Stage7Application.class, args);
    }

}

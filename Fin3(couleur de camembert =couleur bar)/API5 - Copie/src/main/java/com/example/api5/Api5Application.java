package com.example.api5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class Api5Application {

    public static void main(String[] args) {
        SpringApplication.run(Api5Application.class, args);
    }

}

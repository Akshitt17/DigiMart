package com.smarthome.stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableJpaAuditing
public class StockServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockServiceApplication.class, args);
        System.out.println("=================================");
        System.out.println("Stock Service Started Successfully!");
        System.out.println("Port: 8083");
        System.out.println("NO Role Checks - Gateway Handles All!");
        System.out.println("=================================");
    }
}

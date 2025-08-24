package com.smarthome.gateway.config;

import com.smarthome.gateway.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // User Service Routes
                .route("user-service", r -> r.path("/api/v1/auth/**")
                        .uri("lb://user-service"))
                
                // Device Service Routes  
                .route("device-service", r -> r.path("/api/v1/devices/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("lb://device-service"))
                
                // Stock Service Routes
                .route("stock-service", r -> r.path("/api/v1/stock/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("lb://stock-service"))
                
                // Customer Service Routes
                .route("customer-service", r -> r.path("/api/v1/customers/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("lb://customer-service"))
                
                // Order Service Routes
                .route("order-service", r -> r.path("/api/v1/orders/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("lb://customer-service"))
                
                // Payment Service Routes
                .route("payment-service", r -> r.path("/api/v1/payments/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("lb://payment-service"))
                
                // Invoice Service Routes
                .route("payment-service", r -> r.path("/api/v1/reports/**")
                        .filters(f -> f.filter(jwtAuthenticationFilter.apply(new JwtAuthenticationFilter.Config())))
                        .uri("lb://payment-service"))
                
                .build();
    }
}

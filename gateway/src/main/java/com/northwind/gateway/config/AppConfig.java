package com.northwind.gateway.config;

import com.northwind.gateway.service.RouteValidatorService;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {
    public AppConfig() {
    }

    @Bean
    public RouteValidatorService validator() {
        return new RouteValidatorService();
    }

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }
}

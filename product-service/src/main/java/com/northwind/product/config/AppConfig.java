package com.northwind.product.config;

import com.northwind.product.filter.ApiKeyAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class AppConfig {
    @Bean
    public ApiKeyAuthenticationFilter apiKeyAuthenticationFilter() {
        return new ApiKeyAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                authorize -> authorize
                        .requestMatchers(HttpMethod.POST,"/product-service/product").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/product-service/product/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/product-service/product/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/product-service/category").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/product-service/category/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/product-service/category/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/product-service/supplier").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/product-service/supplier/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/product-service/supplier/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
        );
        http.addFilterBefore(apiKeyAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.csrf().disable();
        return http.build();
    }
}

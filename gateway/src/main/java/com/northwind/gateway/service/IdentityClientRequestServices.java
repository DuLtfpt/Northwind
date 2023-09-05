package com.northwind.gateway.service;

import com.northwind.gateway.exception.ErrorResponse;
import com.northwind.gateway.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
public class IdentityClientRequestServices {
    @Autowired
    private WebClient.Builder webClientBuilder;
    @Value("${request.header.api-key}")
    private String apiKey;
    @Value("${service.auth.domain}")
    private String identityDomain;
    private final String QUERYPATH = "/auth/verify?token=";
    @Value("${service.auth.api-key}")
    private String identityKey;

    public Mono<User> exchangeToken(String token) {
        return webClientBuilder
                .build()
                .get()
                .uri(identityDomain + QUERYPATH + token)
                .header(apiKey, identityKey)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response
                        .bodyToMono(ErrorResponse.class)
                        .flatMap(body -> Mono.error(new ResponseStatusException(body.getStatus(),body.getMessage()))
                ))
                .bodyToMono(User.class);
    }
}

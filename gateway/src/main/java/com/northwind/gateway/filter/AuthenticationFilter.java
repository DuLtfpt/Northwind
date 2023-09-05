package com.northwind.gateway.filter;

import com.northwind.gateway.model.User;
import com.northwind.gateway.service.IdentityClientRequestServices;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    private final String PREFIXBEARER = "Bearer ";
    private final String UIDHEADER = "user-uid";
    private final String ROLEHEADER = "user-role";
    @Autowired
    private WebClient.Builder webClientBuilder;
    @Autowired
    private IdentityClientRequestServices identityServices;

    @Autowired
    public AuthenticationFilter() {
        super(Config.class);
    }

    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return chain.filter(exchange);
            }

            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            if (authHeader != null && authHeader.startsWith(PREFIXBEARER)) {
                authHeader = authHeader.substring(7);
            }

            Mono<User> response = identityServices.exchangeToken(authHeader);

            return response.flatMap((u) -> {
                ServerHttpRequest request
                        = exchange
                        .getRequest()
                        .mutate()
                        .header(UIDHEADER, u.getUid().toString())
                        .header(ROLEHEADER, u.getRole()).build();
                ServerWebExchange exchange1 = exchange.mutate().request(request).build();
                return chain.filter(exchange1);
            });

        };
    }

     static class Config {
        public Config() {
        }
    }
}
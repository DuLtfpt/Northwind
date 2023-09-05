package com.northwind.gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class ApiAuthenticationFilter extends AbstractGatewayFilterFactory<ApiAuthenticationFilter.ApiAuthenticationConfig> {
    @Autowired
    private Environment env;
    private final String APIKEY = "x-api-key";
    private final String PREFIXENV = "service.";
    private final String POSTFIXENV = ".api-key";

    @Autowired
    public ApiAuthenticationFilter() {
        super(ApiAuthenticationConfig.class);
    }

    public GatewayFilter apply(ApiAuthenticationConfig config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String resource = request.getURI().getPath().split("/")[1];
            request.mutate()
                    .header(APIKEY,
                            env.getProperty(PREFIXENV + resource + POSTFIXENV)
                    )
                    .build();
            ServerWebExchange exchange1 = exchange.mutate().request(request).build();
            return chain.filter(exchange1);
        };
    }

    static class ApiAuthenticationConfig {
        public ApiAuthenticationConfig() {
        }
    }
}
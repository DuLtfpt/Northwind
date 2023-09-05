package com.northwind.gateway.filter;

import com.northwind.gateway.service.RouteValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class RouteValidatorFilter extends AbstractGatewayFilterFactory<RouteValidatorFilter.RouteValidatorConfig> {
    private final String RESOURCEERRORMESSAGE ="Resource not found";
    @Autowired
    public RouteValidatorFilter() {
        super(RouteValidatorConfig.class);
    }
    @Autowired
    private RouteValidatorService validator;

    @Override
    public GatewayFilter apply(RouteValidatorConfig config) {
        return (exchange, chain) ->{
            if(validator.isSecured.test(exchange.getRequest())){
                return chain.filter(exchange);
            }else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,RESOURCEERRORMESSAGE);
            }
        };
    }

    static class RouteValidatorConfig {
        public RouteValidatorConfig() {
        }
    }
}

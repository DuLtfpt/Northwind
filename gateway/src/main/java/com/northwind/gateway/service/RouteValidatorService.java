package com.northwind.gateway.service;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
public class RouteValidatorService {
    public static final List<String> openApiEndpoints = List.of(
            "/auth/login",
            "/auth/registry",
            "/auth/change-password",
            "/product-service/category",
            "/product-service/product",
            "/product-service/supplier",
            "/order-service/order",
            "/order-service/user"
    );
    public Predicate<ServerHttpRequest> isSecured =
            (request) -> openApiEndpoints
                    .stream()
                    .anyMatch((uri) -> request
                            .getURI()
                            .getPath()
                            .contains(uri));

    public RouteValidatorService() {
    }
}
package com.northwind.orderservice.service;

import com.northwind.orderservice.model.ApiKeyAuthentication;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

@Service
@PropertySource({"classpath:application.yml","classpath:message.properties"})
public class ApiKeyAuthenticationService {
    @Value("${request.header.api-key}")
    private String apiKeyHeader;
    @Value("${request.header.uid}")
    private String uidHeader;
    @Value("${request.header.role}")
    private String roleHeader;
    @Value("${service.order-service.api-key}")
    private String orderKey;
    @Value("${message.invalid-api-key}")
    private String invalidApiKey;
    @Value("${message.uid-not-found}")
    private String uidNotFound;
    @Value("${message.role-not-found}")
    private String roleNotFound;
    private final String PREFIXROLE = "ROLE_";


    public Authentication getAuthentication(HttpServletRequest request) {
        String apiKey = request.getHeader(apiKeyHeader);
        if (!orderKey.equals(apiKey)) {
            throw new BadCredentialsException(invalidApiKey);
        }
        String uid = request.getHeader(uidHeader);
        if(null==uid){
            throw new BadCredentialsException(uidNotFound);
        }
        String role = request.getHeader(roleHeader);
        if(null==role){
            throw new BadCredentialsException(roleNotFound);
        }
        return new ApiKeyAuthentication(uid, AuthorityUtils.createAuthorityList(PREFIXROLE+role));

    }
}

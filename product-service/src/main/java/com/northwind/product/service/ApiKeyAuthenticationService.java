package com.northwind.product.service;

import com.northwind.product.model.ApiKeyAuthentication;
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
    @Value("${service.product-service.api-key}")
    private String productServiceKey;
    @Value("${message.invalid-api-key}")
    private String invalidApiKey;
    @Value("${message.role-not-found}")
    private String roleNotFound;
    private final String PREFIX_ROLE = "ROLE_";

    public Authentication getAuthentication(HttpServletRequest request) {
        String apiKey = request.getHeader(apiKeyHeader);
        if (!productServiceKey.equals(apiKey)) {
            throw new BadCredentialsException(invalidApiKey);
        }
        String uid = request.getHeader(uidHeader);
        if(null==uid){
            return new ApiKeyAuthentication(null, AuthorityUtils.NO_AUTHORITIES);
        }
        String role = request.getHeader(roleHeader);
        if(null==role){
            throw new BadCredentialsException(roleNotFound);
        }
        return new ApiKeyAuthentication(uid, AuthorityUtils.createAuthorityList(PREFIX_ROLE+role));
    }
}

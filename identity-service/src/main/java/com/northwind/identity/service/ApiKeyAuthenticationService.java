package com.northwind.identity.service;

import com.northwind.identity.model.ApiKeyAuthentication;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

@Service
public class ApiKeyAuthenticationService {
    @Value("${request.header.api-key}")
    private String apiKey;
    @Value("${service.api-key.auth}")
    private String identityKey;

    public Authentication getAuthentication(HttpServletRequest request) {
        String apiKey = request.getHeader(this.apiKey);
        if (!identityKey.equals(apiKey)) {
            throw new BadCredentialsException("Invalid API Key");
        }
        return new ApiKeyAuthentication(apiKey, AuthorityUtils.NO_AUTHORITIES);

    }
}

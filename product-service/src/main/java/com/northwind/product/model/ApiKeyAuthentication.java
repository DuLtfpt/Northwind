package com.northwind.product.model;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class ApiKeyAuthentication extends AbstractAuthenticationToken {
    private final String uid;

    public ApiKeyAuthentication(String uid, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.uid = uid;
        this.setAuthenticated(true);
    }

    public Object getCredentials() {
        return null;
    }

    public Object getPrincipal() {
        return this.uid;
    }
}

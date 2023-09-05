package com.northwind.identity.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UsernamePasswordRequest {
    private String username;
    private String password;
}

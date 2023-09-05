package com.northwind.identity.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PasswordChangeRequest {
    private String username;
    private String oldPassword;
    private String password;
}

package com.northwind.identity.controller;

import com.northwind.identity.model.User;
import com.northwind.identity.request.PasswordChangeRequest;
import com.northwind.identity.request.UsernamePasswordRequest;
import com.northwind.identity.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/auth"})
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping({"/login"})
    public ResponseEntity<?> getToken(@RequestBody @Valid UsernamePasswordRequest request) {
        String response = authService.authenticateUser(request);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping({"/verify"})
    public ResponseEntity<?> verifyToken(@RequestParam(name = "token") String token) {
        User response = this.authService.exchangeToken(token);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping({"/registry"})
    public ResponseEntity<?> createUser(@RequestBody @Valid UsernamePasswordRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        String response = this.authService.registryUser(user);
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping({"/change-password"})
    public ResponseEntity<?> updateUser(@RequestBody @Valid PasswordChangeRequest request) {
        String response = this.authService.changePassword(request);
        return ResponseEntity.ok().body(response);
    }
}

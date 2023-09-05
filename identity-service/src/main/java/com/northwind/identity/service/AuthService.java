package com.northwind.identity.service;

import com.northwind.identity.exception.BadRequestException;
import com.northwind.identity.exception.NotFoundException;
import com.northwind.identity.model.CustomUserDetail;
import com.northwind.identity.model.User;
import com.northwind.identity.repository.UserRepository;
import com.northwind.identity.request.PasswordChangeRequest;
import com.northwind.identity.request.UsernamePasswordRequest;
import com.northwind.identity.ulti.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private JwtTokenUtil tokenUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private Environment env;
    private final String SUBSEPARATOR = ":";

    public String changePassword(PasswordChangeRequest request) {
        User user = repository.findByUsername(request.getUsername());

        if (null == user) {
            throw new NotFoundException("User: " + request.getUsername() + " not found");
        }

        if (!encoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BadRequestException("Old password not match");
        }

        user.setPassword(encoder.encode(request.getPassword()));
        repository.save(user);
        return "Password change successfully";
    }

    public String registryUser(User user) {
        User temp = repository.findByUsername(user.getUsername());

        if (null != temp) {
            throw new BadRequestException("Username: " + user.getUsername() + " is taken");
        }

        user.setRole("USER");
        user.setPassword(encoder.encode(user.getPassword()));
        repository.save(user);
        return accessToken(user);

    }

    public String authenticateUser(UsernamePasswordRequest request) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()
                        )
                );
        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
        User user = userDetail.getUsers();
        return accessToken(user);
    }

    public User exchangeToken(String token) {
        tokenUtil.validateToken(token);
        Map<String, String> subject = tokenUtil.getSubjectFromJWT(token, (s) -> {
            Map<String, String> map = new HashMap();
            String[] sub = s.split(":");
            map.put("uid", sub[0]);
            map.put("role", sub[1]);
            return map;
        });

        User user = repository.findById(UUID
                        .fromString(subject.get("uid")))
                .orElse(null);

        if (null == user) {
            throw new NotFoundException("User: " + (String) subject.get("uid") + " not found");
        }
        user.setPassword((String) null);
        return user;

    }

    private String accessToken(User user) {
        return tokenUtil.generateToken(
                String.format(
                        "%s%s%s",
                        user.getUid(),
                        ":",
                        user.getRole()
                )
        );
    }
}

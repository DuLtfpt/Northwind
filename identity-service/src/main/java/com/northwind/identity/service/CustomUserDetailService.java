package com.northwind.identity.service;

import com.northwind.identity.exception.NotFoundException;
import com.northwind.identity.model.CustomUserDetail;
import com.northwind.identity.model.User;
import com.northwind.identity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username);
        if (null == user) {
            throw new NotFoundException("User: " + username + " not found");
        }

        return new CustomUserDetail(user);
    }
}

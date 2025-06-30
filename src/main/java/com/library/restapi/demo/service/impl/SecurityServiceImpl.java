package com.library.restapi.demo.service.impl;

import com.library.restapi.demo.service.SecurityService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

    private final PasswordEncoder passwordBcryptEncoder;

    public SecurityServiceImpl() {
        this.passwordBcryptEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public String hashPasswordBcrypt(String password) {
        return passwordBcryptEncoder.encode(password);
    }

    @Override
    public boolean checkPassword(String rawPassword, String hashedPassword) {
        return passwordBcryptEncoder.matches(rawPassword, hashedPassword);
    }
}

package com.library.restapi.demo.service;

public interface SecurityService {

    String hashPasswordBcrypt(String password);

    boolean checkPassword(String rawPassword, String hashedPassword);
}

package com.example.demo.security;

import lombok.Getter;
import lombok.Setter;

@Getter
public class AccountCredentials {
    private String username;
    private String password;

    @Override
    public String toString() {
        return "AccountCredentials [username=" + username + ", password=" + password + "]";
    }
}
package com.example.projectprm392.model;

public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String fullName;


    public RegisterRequest(String username, String email, String password, String fullName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
    }
}

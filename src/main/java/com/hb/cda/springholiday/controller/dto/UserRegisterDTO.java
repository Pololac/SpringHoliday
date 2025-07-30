package com.hb.cda.springholiday.controller.dto;

import jakarta.validation.constraints.*;

public class UserRegisterDTO {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 4, max = 64)
    private String password;

    public UserRegisterDTO() {
    }

    public UserRegisterDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

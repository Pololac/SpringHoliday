package com.hb.cda.springholiday.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginCredentialsDTO {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}

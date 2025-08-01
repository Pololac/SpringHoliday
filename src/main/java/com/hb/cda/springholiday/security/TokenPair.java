package com.hb.cda.springholiday.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenPair {

    private String refreshToken;
    private String jwt;
}


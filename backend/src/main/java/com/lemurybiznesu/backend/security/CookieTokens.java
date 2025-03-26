package com.lemurybiznesu.backend.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CookieTokens {
    private String accessToken;
    private String refreshToken;
}

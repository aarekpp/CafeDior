package com.lemurybiznesu.backend.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenDetails {
    private String userId;
    private Date expirationDate;
    private String role;
    private Integer tokenVersion;
}

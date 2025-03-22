package com.lemurybiznesu.backend.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SigninRequest {
    @NotBlank @Email
    String email;
    @NotBlank @Size(min = 10, max = 30)
    String password;
}

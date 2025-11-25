package com.bc46.trabea.auth.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class AuthPartTimeRegisterResponse {
    private final String workEmail;
    private final String firstName;
    private final String lastName;
}

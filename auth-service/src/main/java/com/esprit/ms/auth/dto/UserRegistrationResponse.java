package com.esprit.ms.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for user registration response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationResponse {

    private boolean success;
    private String message;
    private String userId;

    public static UserRegistrationResponse success(String userId) {
        return new UserRegistrationResponse(true, "User registered successfully", userId);
    }

    public static UserRegistrationResponse error(String message) {
        return new UserRegistrationResponse(false, message, null);
    }
}

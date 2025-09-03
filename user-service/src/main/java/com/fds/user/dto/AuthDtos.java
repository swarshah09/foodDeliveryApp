package com.fds.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDtos {

    public static class RegisterRequest {
        @Email @NotBlank
        public String email;
        @NotBlank @Size(min = 6, max = 100)
        public String password;
        public String role; // optional: ADMIN, CUSTOMER, RESTAURANT_OWNER, DELIVERY_AGENT
    }

    public static class LoginRequest {
        @Email @NotBlank
        public String email;
        @NotBlank
        public String password;
    }

    public static class AuthResponse {
        public String token;
        public Long userId;
        public String role;
        public AuthResponse(String token, Long userId, String role) {
            this.token = token; this.userId = userId; this.role = role;
        }
    }
}


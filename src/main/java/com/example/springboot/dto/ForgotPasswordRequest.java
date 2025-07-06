package com.example.springboot.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Forgot password request payload")
public class ForgotPasswordRequest {
    
    @Schema(description = "Username or email of the user", example = "john_doe")
    @NotBlank
    private String username;

    // Getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}

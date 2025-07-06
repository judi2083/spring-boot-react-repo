package com.example.springboot.dto;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Payload to reset user password with a token")
public class ResetPasswordRequest {
    
    @NotBlank
    @Schema(description = "Reset token sent to user", example = "abc123token")
    private String token;

    @NotBlank
    @Schema(description = "New password", example = "newSecurePass")
    private String newPassword;

    // Getters and setters
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

package com.example.springboot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Payload to change user password")
public class ChangePasswordRequest {

    @NotBlank(message = "Old password is required")
    @Schema(description = "Current password of the user", example = "oldPassword123")
    private String oldPassword;

    @NotBlank(message = "New password is required")
    @Schema(description = "New password to be set", example = "newPassword456")
    private String newPassword;

    // Getters and Setters
    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

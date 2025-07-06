package com.example.springboot.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Payload for admin to change a user's password")
public class AdminChangePasswordRequest {

    @Schema(description = "New password to be set", example = "NewP@ssword123")
    private String newPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

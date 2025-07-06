package com.example.springboot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Schema(description = "User registration request payload")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDto {

    @NotBlank(message = "Username is mandatory")
    @Schema(description = "Unique username for login", example = "john_doe")
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Schema(description = "Password for the user account", example = "P@ssw0rd")
    private String password;

    @Email
    @Schema(description = "User's email address", example = "john@example.com")
    private String email;
   
    @Schema(description = "Role of the user (default is USER)", example = "USER")
    private String role; // Optional: default to "USER"
}
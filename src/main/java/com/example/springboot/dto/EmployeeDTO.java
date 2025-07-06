package com.example.springboot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Schema(description = "Data Transfer Object representing an employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {

    @Schema(description = "Auto-generated employee ID", example = "101", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "First name is mandatory")
    @Size(min = 2, message = "First name must be at least 2 characters")
    @Schema(description = "First name of the employee", example = "John")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Schema(description = "Last name of the employee", example = "Doe")
    private String lastName;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Schema(description = "Email address of the employee", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Department name (optional)", example = "Finance", nullable = true)
    private String department;

    @NotBlank(message = "Role is mandatory")
    @Schema(description = "Role or designation", example = "Software Engineer")
    private String role;
}

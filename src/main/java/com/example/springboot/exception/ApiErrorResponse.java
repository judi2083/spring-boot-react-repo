package com.example.springboot.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Standard error response for failed API requests")
public class ApiErrorResponse {

    @Schema(description = "Timestamp when the error occurred", example = "2025-06-29T17:50:30")
    private LocalDateTime timestamp = LocalDateTime.now();

    @Schema(description = "HTTP status code", example = "400")
    private int status;

    @Schema(description = "Short error description", example = "Bad Request")
    private String error;

    @Schema(description = "The request path that caused the error", example = "/api/employees")
    private String path;

    @Schema(description = "List of detailed error messages", example = "[\"First name is mandatory\", \"Role is mandatory\"]")
    private List<String> messages;

    // Optional: You can keep this if needed elsewhere
    public ApiErrorResponse(int status, String error, String path, List<String> messages) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.path = path;
        this.messages = messages;
    }
}

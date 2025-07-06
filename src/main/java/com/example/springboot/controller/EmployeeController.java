package com.example.springboot.controller;

import com.example.springboot.dto.EmployeeDTO;
import com.example.springboot.exception.ApiErrorResponse;
import com.example.springboot.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Employee Management", description = "Operations related to Employee CRUD functionality")
@Slf4j
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @Operation(
        summary = "Get all employees (unpaged)",
        description = "Returns the complete list of employees without pagination."
    )
    @GetMapping("/all")
    public List<EmployeeDTO> getAll() {
        log.info("Fetching all employees");
        return service.getAllEmployees();
    }

    @Operation(
        summary = "Get employees with pagination",
        description = "Returns a paginated list of employees. Defaults to 5 records per page, sorted by ID."
    )
    @GetMapping
    public Page<EmployeeDTO> getEmployees(@PageableDefault(size = 5, sort = "id") Pageable pageable) {
        return service.getAllEmployees(pageable);
    }

    @Operation(summary = "Get employee by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the employee"),
        @ApiResponse(responseCode = "404", description = "Employee not found",
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Access denied")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getById(@PathVariable Long id) {
        log.info("Fetching employee with id: {}", id);
        return ResponseEntity.ok(service.getEmployeeById(id));
    }

    @Operation(summary = "Create a new employee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Employee created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input provided",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Access denied")
    })
    @PostMapping
    // public ResponseEntity<EmployeeDTO> create(@Valid @RequestBody EmployeeDTO employeeDTO) {
    //     log.info("Creating new employee: {}", employeeDTO.getEmail());
    //     return ResponseEntity.ok(service.createEmployee(employeeDTO));
    // }
    public ResponseEntity<EmployeeDTO> create(@Valid @RequestBody EmployeeDTO employeeDTO) {
        log.info("Creating new employee: {}", employeeDTO.getEmail());
        return ResponseEntity
            .status(HttpStatus.CREATED) // ✅ Set proper status
            .body(service.createEmployee(employeeDTO));
    }

    @Operation(summary = "Update an existing employee by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employee updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input provided",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Employee not found",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Access denied")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> update(@PathVariable Long id, @Valid @RequestBody EmployeeDTO employeeDTO) {
        log.info("Updating employee with id : {}", id);
        return ResponseEntity.ok(service.updateEmployee(id, employeeDTO));
    }

    @Operation(summary = "Delete an employee by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Employee deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Employee not found",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Access denied")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
         log.warn("Deleting employee with id: {}", id);
        service.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}

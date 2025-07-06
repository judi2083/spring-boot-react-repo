package com.example.springboot.controller;

import com.example.springboot.dto.AdminChangePasswordRequest;
import com.example.springboot.entity.User;
import com.example.springboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import java.util.Optional;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User Management", description = "Operations for admin to manage users")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ Get all users (Admin only)
    @Operation(summary = "Get all users", description = "Returns all users. Admin access required.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Users fetched successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
   @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers(Authentication auth) {
        System.out.println("Roles: " + auth.getAuthorities());
        return userRepository.findAll();
    }

    // ✅ Update user by ID
    @Operation(summary = "Update user by ID", description = "Updates username and role. Admin access required.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return userRepository.findById(id)
            .map(user -> {
                user.setUsername(updatedUser.getUsername());
                user.setRole(updatedUser.getRole());
                userRepository.save(user);
                return ResponseEntity.ok("User updated successfully");
            }).orElse(ResponseEntity.notFound().build());
    }

    // ✅ Delete user by ID
    // @DeleteMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    // public ResponseEntity<?> deleteUser(@PathVariable Long id) {
    //     if (!userRepository.existsById(id)) {
    //         return ResponseEntity.notFound().build();
    //     }
    //     userRepository.deleteById(id);
    //     return ResponseEntity.ok("User deleted successfully");
    // }

    // ✅ Delete user by ID
    @Operation(summary = "Delete user by ID", description = "Deletes user unless it's the logged-in admin themselves.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User deleted successfully"),
        @ApiResponse(responseCode = "403", description = "Admins cannot delete their own account"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, Authentication auth) {
        Optional<User> userToDelete = userRepository.findById(id);
        if (userToDelete.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        String loggedInUsername = auth.getName();
        if (userToDelete.get().getUsername().equals(loggedInUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Admins cannot delete their own account.");
        }

        userRepository.deleteById(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    
    // @PutMapping("/{id}/change-password")
    // @PreAuthorize("hasRole('ADMIN')")
    // public ResponseEntity<?> changeUserPassword(
    //         @PathVariable Long id,
    //         @RequestBody Map<String, String> payload) {

    //     String newPassword = payload.get("newPassword");

    //     System.out.println("newPassword::"+newPassword);

    //     if (newPassword == null || newPassword.trim().isEmpty()) {
    //         return ResponseEntity.badRequest().body("New password must not be empty");
    //     }

    //     Optional<User> optionalUser = userRepository.findById(id);
    //     if (optionalUser.isEmpty()) {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    //     }

    //     User user = optionalUser.get();
    //     user.setPassword(passwordEncoder.encode(newPassword)); // encrypt!
    //     userRepository.save(user);

    //     return ResponseEntity.ok("Password updated successfully");
    // }
    @Operation(summary = "Change user password by ID", description = "Allows admin to change password of any user.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Password updated successfully"),
        @ApiResponse(responseCode = "400", description = "New password is empty"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{id}/change-password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> changeUserPassword(
            @PathVariable Long id,
            @RequestBody AdminChangePasswordRequest payload) {

        String newPassword = payload.getNewPassword();

        if (newPassword == null || newPassword.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("New password must not be empty");
        }

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = optionalUser.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok("Password updated successfully");
    }
}

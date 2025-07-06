package com.example.springboot.controller;

import com.example.springboot.dto.AuthRequest;
import com.example.springboot.dto.ChangePasswordRequest;
import com.example.springboot.dto.ForgotPasswordRequest;
import com.example.springboot.dto.ResetPasswordRequest;
import com.example.springboot.dto.UserRegistrationDto;
import com.example.springboot.entity.User;
import com.example.springboot.repository.UserRepository;
import com.example.springboot.service.MailService;
import com.example.springboot.service.PasswordResetService;
import com.example.springboot.service.TokenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


// @RestController
// @RequestMapping("/auth")
// public class AuthController {

//     @Autowired
//     private AuthenticationManager authenticationManager;

//     @Autowired
//     private TokenService tokenService;

//     @PostMapping("/token")
//     public ResponseEntity<?> getToken(@RequestBody AuthRequest authRequest) {
       
//         try {
//             Authentication authentication = authenticationManager.authenticate(
//                 new UsernamePasswordAuthenticationToken(
//                     authRequest.getUsername(), authRequest.getPassword())
//             );

//             String role = authentication.getAuthorities().stream()
//                     .findFirst()
//                     .map(granted -> granted.getAuthority().replace("ROLE_", ""))
//                     .orElse("USER");

//             String token = tokenService.generateToken(authRequest.getUsername(), role);
         
//             return ResponseEntity.ok(Collections.singletonMap("token", token));

//         } catch (AuthenticationException e) {
//             System.err.println("Login failed for user: " + authRequest.getUsername());
//             return ResponseEntity.status(401).body("Invalid username or password");
//         }
//     }


// }


@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private MailService mailService;
    
    @Operation(
        summary = "Generate JWT token",
        description = "Authenticates the user using username and password, and returns a signed JWT token on success."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Token generated successfully"),
        @ApiResponse(responseCode = "401", description = "Invalid username or password")
    })
    @PostMapping("/token")
    public ResponseEntity<?> getToken(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    authRequest.getUsername(), authRequest.getPassword())
            );

            String role = authentication.getAuthorities().stream()
                    .findFirst()
                    .map(granted -> granted.getAuthority().replace("ROLE_", ""))
                    .orElse("USER");

            log.info("AuthController:getToken-->role::"+role);

            String token = tokenService.generateToken(authRequest.getUsername(), role);
            return ResponseEntity.ok(Collections.singletonMap("token", token));

        } catch (AuthenticationException e) {
            System.err.println("Login failed for user: " + authRequest.getUsername());
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    @Operation(
        summary = "Register a new user",
        description = "Creates a new user account with the provided username, email, and password. Role is optional and defaults to USER."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "User registered successfully"),
        @ApiResponse(responseCode = "409", description = "Username already exists"),
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDto request) {
        Optional<User> existingUser = userRepository.findByUsername(request.getUsername());

        //log.info("existingUser::"+existingUser);

        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Username already exists");
        }

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole("USER"); // default role

        userRepository.save(newUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User registered successfully");
    }

    @Operation(summary = "Change password for authenticated user", description = "Requires old and new password. User must be logged in.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Password changed successfully"),
        @ApiResponse(responseCode = "400", description = "Old password incorrect or validation error"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> changePassword(
        Authentication authentication,
        @Valid @RequestBody ChangePasswordRequest request) {
    
        String username = authentication.getName();
        String oldPassword = request.getOldPassword();
        String newPassword = request.getNewPassword();

        log.info("userName::"+username);
        log.info("oldPassword::"+oldPassword);
        log.info("newPassword::"+newPassword);

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = optionalUser.get();
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return ResponseEntity.badRequest().body("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok("Password changed successfully");
    }

    @Operation(
        summary = "Initiate password reset via username or email",
        description = "Generates a password reset token for the user identified by username or email. In real applications, this token should be sent via email."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reset token generated successfully"),
        @ApiResponse(responseCode = "400", description = "Username or email is empty"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        String username = request.getUsername(); 

        log.info("forgotPassword::userName=="+username);

        if (username == null || username.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Username or email is required");
        }

       // Optional<User> optionalUser = userRepository.findByUsername(username);
        Optional<User> optionalUser = userRepository.findByUsernameOrEmail(username, username);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = optionalUser.get();
        String token = passwordResetService.createPasswordResetToken(user);

        // TODO: In production, email this link to the user
        String resetUrl = "http://localhost:3000/reset-password?token=" + token;
        log.info("Generated password reset URL: {}", resetUrl);

        // ✅ Send email
        mailService.sendPasswordResetEmail(user.getEmail(), resetUrl);

        // ✅ In real apps, you'd send this token via email
        //return ResponseEntity.ok("Reset token: " + token);
        return ResponseEntity.ok("Password reset link has been sent to your email.");
    }

    // @PostMapping("/reset-password")
    // public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
    //     String token = request.get("token");
    //     String newPassword = request.get("newPassword");
        
    //     log.info("resetPassword::token=="+token);
    //     log.info("resetPassword::newPassword=="+newPassword);

    //     if (!passwordResetService.isValidToken(token)) {
    //         return ResponseEntity.badRequest().body("Invalid or expired token");
    //     }

    //     passwordResetService.updatePassword(token, newPassword);
    //     return ResponseEntity.ok("Password updated successfully");
    // }

    @Operation(summary = "Reset user password using token", description = "Token is generated via forgot-password request")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Password updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid or expired token")
    })
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        String token = request.getToken();
        String newPassword = request.getNewPassword();

        log.info("resetPassword::token=={}", token);
        log.info("resetPassword::newPassword=={}", newPassword);

        if (!passwordResetService.isValidToken(token)) {
            return ResponseEntity.badRequest().body("Invalid or expired token");
        }

        passwordResetService.updatePassword(token, newPassword);
        return ResponseEntity.ok("Password updated successfully");
    }

}


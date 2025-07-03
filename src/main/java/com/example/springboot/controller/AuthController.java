package com.example.springboot.controller;

import com.example.springboot.dto.AuthRequest;
import com.example.springboot.entity.User;
import com.example.springboot.repository.UserRepository;
import com.example.springboot.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

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

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

            System.out.println("AuthController:getToken-->role::"+role);

            String token = tokenService.generateToken(authRequest.getUsername(), role);
            return ResponseEntity.ok(Collections.singletonMap("token", token));

        } catch (AuthenticationException e) {
            System.err.println("Login failed for user: " + authRequest.getUsername());
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    // ✅ Register Endpoint
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AuthRequest request) {
        Optional<User> existingUser = userRepository.findByUsername(request.getUsername());

        System.out.println("existingUser::"+existingUser);

        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Username already exists");
        }

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole("USER"); // default role

        userRepository.save(newUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User registered successfully");
    }
}


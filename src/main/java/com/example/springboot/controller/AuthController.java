package com.example.springboot.controller;

import com.example.springboot.dto.AuthRequest;
import com.example.springboot.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Collections;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/token")
    public ResponseEntity<?> getToken(@RequestBody AuthRequest authRequest) {
        System.out.println("Login username: " + authRequest.getUsername());
        System.out.println("Login password: " + authRequest.getPassword());
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    authRequest.getUsername(), authRequest.getPassword())
            );

            String role = authentication.getAuthorities().stream()
                    .findFirst()
                    .map(granted -> granted.getAuthority().replace("ROLE_", ""))
                    .orElse("USER");

            String token = tokenService.generateToken(authRequest.getUsername(), role);
            System.out.println("getToken::::::;"+token);
            return ResponseEntity.ok(Collections.singletonMap("token", token));

        } catch (AuthenticationException e) {
            System.out.println("Login failed for user: " + authRequest.getUsername());
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }


}

package com.example.springboot.service;

import com.example.springboot.entity.User;

public interface PasswordResetService {
    String createPasswordResetToken(User user);
    boolean isValidToken(String token);
    void updatePassword(String token, String newPassword);
}

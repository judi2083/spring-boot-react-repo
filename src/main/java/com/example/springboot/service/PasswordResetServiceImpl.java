package com.example.springboot.service;

import com.example.springboot.entity.PasswordResetToken;
import com.example.springboot.entity.User;
import com.example.springboot.repository.PasswordResetTokenRepository;
import com.example.springboot.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private static final Logger log = LoggerFactory.getLogger(PasswordResetServiceImpl.class);

    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional //(this ensures both delete and save operations happen within a single database transaction.)
    public String createPasswordResetToken(User user) {

        //safely removes any previous token rows.
        int deleted = tokenRepository.deleteByUser(user);
        log.info("Deleted {} existing reset tokens for user {}", deleted, user.getUsername());

        //ensures uniqueness of the reset token.
        String token = UUID.randomUUID().toString();  
        PasswordResetToken resetToken = new PasswordResetToken(null, token, user, LocalDateTime.now().plusMinutes(30));

        tokenRepository.save(resetToken);

        return token;
    }

    @Override
    public boolean isValidToken(String token) {
        return tokenRepository.findByToken(token)
                .filter(t -> t.getExpiryDate().isAfter(LocalDateTime.now()))
                .isPresent();
    }

    @Override
    @Transactional
    public void updatePassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new EntityNotFoundException("Invalid reset token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Reset token has expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        tokenRepository.delete(resetToken); // token used, delete it
    }
}

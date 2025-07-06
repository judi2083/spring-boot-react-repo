package com.example.springboot.repository;

import com.example.springboot.entity.PasswordResetToken;
import com.example.springboot.entity.User;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    
    Optional<PasswordResetToken> findByToken(String token);

    @Modifying  //tells Spring Data JPA that it's a modifying query (not a select).
    @Transactional //ensures the operation runs inside a transaction.
    @Query("DELETE FROM PasswordResetToken t WHERE t.user = :user")
    int deleteByUser(@Param("user") User user);
}

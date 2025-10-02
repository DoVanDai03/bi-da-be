package com.fpt_be.fpt_be.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fpt_be.fpt_be.Entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    User findByEmail(String email);
    User findByVerificationToken(String verificationToken);
    User findByResetPasswordToken(String resetPasswordToken);
}

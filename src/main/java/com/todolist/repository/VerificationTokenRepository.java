package com.todolist.repository;

import com.todolist.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    VerificationToken findVerificationTokenByUsernameAndCode(String username, UUID code);

    boolean existsVerificationTokenByUsernameAndCode(String username, UUID code);

    void deleteVerificationTokenByUsername(String username);

    void deleteVerificationTokenByCode(UUID code);
}

package com.todolist.repository;

import com.todolist.entity.VerificationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VerificationTokenRepository extends JpaRepository<VerificationTokenEntity, Long> {

    VerificationTokenEntity findVerificationTokenEntityByUsernameAndCode(String username, UUID code);

    boolean existsVerificationTokenEntityByUsernameAndCode(String username, UUID code);

    void deleteVerificationTokenEntityByUsername(String username);

    void deleteVerificationTokenEntityByCode(UUID code);
}

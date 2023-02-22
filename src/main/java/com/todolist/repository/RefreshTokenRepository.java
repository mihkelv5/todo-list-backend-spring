package com.todolist.repository;

import com.todolist.entity.token.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    RefreshToken findRefreshTokensByUserId(UUID userId);

    void deleteById(UUID tokenId);

}

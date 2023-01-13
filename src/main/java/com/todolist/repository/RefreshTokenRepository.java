package com.todolist.repository;

import com.todolist.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {

    RefreshTokenEntity findRefreshTokenEntitiesByUserId(UUID userId);


}

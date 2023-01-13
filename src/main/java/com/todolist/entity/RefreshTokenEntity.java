package com.todolist.entity;

import com.todolist.constant.SecurityConstant;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name="refresh_token")
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    UUID id;
    UUID userId;
    Date expirationDate;

    public RefreshTokenEntity(UUID userId) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.expirationDate = Date.from(Instant.now().plusSeconds(SecurityConstant.REFRESH_EXPIRATION_TIME));
        // token lasts for 7 days
    }

    public RefreshTokenEntity() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}

package com.todolist.entity;

import com.todolist.constant.SecurityConstant;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
@Entity
public class VerificationTokenEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true, nullable = false)
    private UUID code;
    private String username;
    private Date expirationDate;

    public VerificationTokenEntity(String username) {
        this.username = username;
        this.code = UUID.randomUUID();
        this.expirationDate = Date.from(Instant.now().plusSeconds(SecurityConstant.REGISTRATION_TOKEN_EXPIRATION_TIME));
    }

    public VerificationTokenEntity() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getCode() {
        return code;
    }

    public void setCode(UUID code) {
        this.code = code;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}

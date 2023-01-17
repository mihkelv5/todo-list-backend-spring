package com.todolist.entity;

import com.todolist.constant.SecurityConstant;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
@Entity
public class VerificationToken implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(unique = true, nullable = false)
    private UUID code;
    private String username;
    private Date expirationDate;

    public VerificationToken(String username) {
        this.username = username;
        this.expirationDate = Date.from(Instant.now().plusSeconds(SecurityConstant.REGISTRATION_TOKEN_EXPIRATION_TIME));
    }

    public VerificationToken() {}


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

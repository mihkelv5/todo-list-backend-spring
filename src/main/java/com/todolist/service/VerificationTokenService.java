package com.todolist.service;

import com.todolist.entity.VerificationTokenEntity;
import com.todolist.repository.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;


    public VerificationTokenService(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;

    }

    public VerificationTokenEntity createVerificationToken(String username){
        VerificationTokenEntity token = new VerificationTokenEntity(username);
        return this.verificationTokenRepository.save(token);
    }

    public boolean isTokenValid(String username, UUID code){
        return this.verificationTokenRepository.existsVerificationTokenEntityByUsernameAndCode(username, code);
    }


    @Transactional
    public void deleteTokenByCode(UUID code){
        this.verificationTokenRepository.deleteVerificationTokenEntityByCode(code);
    }

}

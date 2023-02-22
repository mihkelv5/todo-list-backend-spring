package com.todolist.service;

import com.todolist.entity.token.VerificationToken;
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

    public VerificationToken createVerificationToken(String username){
        VerificationToken token = new VerificationToken(username);
        return this.verificationTokenRepository.save(token);
    }

    public boolean isTokenValid(String username, UUID code){
        return this.verificationTokenRepository.existsVerificationTokenByUsernameAndCode(username, code);
    }


    @Transactional
    public void deleteTokenByCode(UUID code){
        this.verificationTokenRepository.deleteVerificationTokenByCode(code);
    }

}

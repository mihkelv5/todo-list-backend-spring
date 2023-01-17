package com.todolist.service;

import com.todolist.entity.RefreshToken;
import com.todolist.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken findTokenById(UUID tokenId){

        Optional<RefreshToken> optionalToken = this.refreshTokenRepository.findById(tokenId);
        if(optionalToken.isPresent()){
            return optionalToken.get();
        }
        throw new BadCredentialsException("Token with id: " + tokenId + " not found");
    }

    public RefreshToken createAndSaveRefreshToken(UUID userId){
        RefreshToken token = new RefreshToken(userId);
        return this.refreshTokenRepository.save(token);
    }

    @Transactional
    public void deleteRefreshTokenById(String tokenId){
        UUID id = UUID.fromString(tokenId);
        this.refreshTokenRepository.deleteById(id);
    }
}

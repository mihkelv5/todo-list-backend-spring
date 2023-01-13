package com.todolist.security.provider;

import com.todolist.entity.RefreshTokenEntity;
import com.todolist.security.authentication.RefreshTokenAuthToken;
import com.todolist.service.RefreshTokenService;
import com.todolist.service.UserDetailsServiceImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;


@Component
public class RefreshTokenAuthProvider implements AuthenticationProvider {

    RefreshTokenService tokenService;
    UserDetailsServiceImpl userService;

    public RefreshTokenAuthProvider(RefreshTokenService tokenService, UserDetailsServiceImpl userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();

        UUID tokenId = UUID.fromString(authentication.getCredentials().toString());

        UserDetails user = this.userService.loadUserByUsername(username);

        RefreshTokenEntity refreshToken = this.tokenService.findTokenById(tokenId); //cant be null otherwise error is thrown by service;

        if(refreshToken.getExpirationDate().compareTo(new Date()) < 0) { //expirationDate is before today
            return null;
        }

        return new RefreshTokenAuthToken(username, null, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return RefreshTokenAuthToken.class.equals(authentication);
    }
}

package com.todolist.security.provider;

import com.todolist.entity.RefreshToken;
import com.todolist.security.authentication.RefreshTokenAuthToken;
import com.todolist.security.userdetails.UserDetailsImpl;
import com.todolist.service.RefreshTokenService;
import com.todolist.service.UserDetailsServiceImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.CredentialsExpiredException;
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

        UserDetailsImpl user = this.userService.loadUserByUsername(username);

        RefreshToken refreshToken = this.tokenService.findTokenById(tokenId); //cant be null otherwise error is thrown by service;

        if(refreshToken.getExpirationDate().compareTo(new Date()) < 0) { //expirationDate is before current time
            throw new CredentialsExpiredException("Token is expired");
        }

        if(!refreshToken.getUserId().equals(user.getId())){
            throw new CredentialsExpiredException("Username and token do not match");
        }

        return new RefreshTokenAuthToken(username, null, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return RefreshTokenAuthToken.class.equals(authentication);
    }
}

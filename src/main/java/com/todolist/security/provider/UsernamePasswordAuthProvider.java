package com.todolist.security.provider;


import com.todolist.security.authentication.UsernamePasswordAuthToken;

import com.todolist.service.UserDetailsServiceImpl;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UsernamePasswordAuthProvider implements AuthenticationProvider {

    BCryptPasswordEncoder encoder;
    UserDetailsServiceImpl userService;

    public UsernamePasswordAuthProvider(BCryptPasswordEncoder encoder, UserDetailsServiceImpl userService) {
        this.encoder = encoder;
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails user = userService.loadUserByUsername(username);

        if(encoder.matches(password, user.getPassword())) {
            return new UsernamePasswordAuthToken(username, password, user.getAuthorities());
        }

        throw new BadCredentialsException("Username or password do not match");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthToken.class.equals(authentication);
    }
}

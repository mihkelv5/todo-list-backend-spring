package com.todolist.security.authentication;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UsernamePasswordAuthToken extends UsernamePasswordAuthenticationToken {

    public UsernamePasswordAuthToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public UsernamePasswordAuthToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}

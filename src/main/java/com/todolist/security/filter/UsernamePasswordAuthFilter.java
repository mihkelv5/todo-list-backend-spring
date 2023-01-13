package com.todolist.security.filter;

import com.todolist.constant.SecurityConstant;
import com.todolist.entity.RefreshTokenEntity;
import com.todolist.security.authentication.UsernamePasswordAuthToken;
import com.todolist.service.RefreshTokenService;
import com.todolist.service.UserDetailsServiceImpl;
import com.todolist.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;

@Component
public class UsernamePasswordAuthFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final RefreshTokenService refreshTokenService;

    public UsernamePasswordAuthFilter(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService, RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String username = request.getHeader("username");
        String password = request.getHeader("password");

        Authentication unauthenticated = new UsernamePasswordAuthToken(username, password);
        authenticationManager.authenticate(unauthenticated); //throws error if user is not authenticated.


        UUID userId = this.userDetailsService.loadUserByUsername(username).getId();
        RefreshTokenEntity refreshToken = this.refreshTokenService.createAndSaveRefreshToken(userId); //TODO: encrypt token
        ResponseCookie refreshCookie = ResponseCookie.from(SecurityConstant.REFRESH_TOKEN, refreshToken.getId().toString())
                .httpOnly(true)
                .secure(false)
                .domain("127.0.0.1")
                .path("/")
                .maxAge(Duration.ofSeconds(SecurityConstant.REFRESH_EXPIRATION_TIME))
                .sameSite("Lax")
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

    filterChain.doFilter(request, response);
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().equals("/auth/login");
    }


}

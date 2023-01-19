package com.todolist.security.filter;

import com.todolist.constant.SecurityConstant;
import com.todolist.security.authentication.RefreshTokenAuthToken;
import com.todolist.security.authentication.UsernamePasswordAuthToken;
import com.todolist.security.userdetails.UserDetailsImpl;
import com.todolist.service.UserDetailsServiceImpl;
import com.todolist.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
public class RefreshTokenFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;

    public RefreshTokenFilter(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Cookie[] cookies = request.getCookies();
            Optional<Cookie> optionalCookie = Arrays.stream(cookies).filter(c -> c.getName().equals(SecurityConstant.REFRESH_TOKEN)).findFirst();
            String username = request.getHeader("username");

            String refreshToken = optionalCookie.get().getValue();

            Authentication authentication = new RefreshTokenAuthToken(username, refreshToken);
            authenticationManager.authenticate(authentication); //throws error if user is not authenticated.


            //generate access token and send it
            UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(username);
            final String accessTokenJwt = jwtUtil.generateAccessToken(userDetails);
            response.setHeader(SecurityConstant.JWT_TOKEN_HEADER, accessTokenJwt);
            filterChain.doFilter(request, response);

        } catch (RuntimeException e) {
            response.setStatus(401);
            response.getWriter().write(e.getMessage());
        }

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().equals("/auth/get-access");
    }
}

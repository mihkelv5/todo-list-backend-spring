package com.todolist.controller;

import com.todolist.constant.SecurityConstant;
import com.todolist.entity.UserModel;
import com.todolist.service.RefreshTokenService;
import com.todolist.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/auth")
public class AuthController {


    private final UserService userService;
    public final RefreshTokenService tokenService;


    public AuthController(UserService userService, RefreshTokenService tokenService) {

        this.userService = userService;

        this.tokenService = tokenService;
    }

    @GetMapping("/login")
    public ResponseEntity<?> createRefreshToken() {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("response", "Refresh token created");
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/get-access")
    public ResponseEntity<UserModel> createAuthenticationToken(HttpServletRequest request) {
        String username = request.getHeader("username");
        UserModel user = this.userService.findUserByUsername(username);
        return ResponseEntity.ok().body(user) ;
    }

    @Transactional
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) { // move more logic to filter?

        Cookie[] cookies = request.getCookies();
        Optional<Cookie> optionalCookie = Arrays.stream(cookies).filter(c -> c.getName().equals(SecurityConstant.REFRESH_TOKEN)).findFirst();
        optionalCookie.ifPresent(cookie -> this.tokenService.deleteRefreshTokenById(cookie.getValue()));
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("response", "logged out");
        ResponseCookie refreshCookie = ResponseCookie.from(SecurityConstant.REFRESH_TOKEN, "")
                .httpOnly(true)
                .secure(false)
                .domain("127.0.0.1")
                .path("/")
                .maxAge(Duration.ofSeconds(1))
                .sameSite("Lax")
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        return ResponseEntity.ok().headers(headers).body(responseBody);
    }

    @PostMapping("/register")
    public ResponseEntity<UserModel> addUser(@RequestBody UserModel user) {
        user.setPassword(new BCryptPasswordEncoder(5).encode(user.getPassword()));
        try {
            userService.addUser(user);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

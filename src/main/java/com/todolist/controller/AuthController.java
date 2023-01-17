package com.todolist.controller;

import com.todolist.SensitiveData;
import com.todolist.constant.SecurityConstant;
import com.todolist.email.EmailServiceImpl;
import com.todolist.entity.UserModel;
import com.todolist.entity.VerificationToken;
import com.todolist.entity.dto.UserLoginDTO;
import com.todolist.service.RefreshTokenService;
import com.todolist.service.UserService;
import com.todolist.service.VerificationTokenService;
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
import java.util.*;

@RestController
@RequestMapping(path = "/auth")
public class AuthController {


    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final VerificationTokenService verificationTokenService;

    private final EmailServiceImpl emailService;


    public AuthController(UserService userService, RefreshTokenService refreshTokenService, VerificationTokenService verificationTokenService, EmailServiceImpl emailService) {

        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.verificationTokenService = verificationTokenService;
        this.emailService = emailService;
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
        optionalCookie.ifPresent(cookie -> this.refreshTokenService.deleteRefreshTokenById(cookie.getValue()));
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
    public ResponseEntity<?> addUser(@RequestBody UserLoginDTO userDTO) {
        UserModel user = new UserModel();

        //getting data from DTO. Could use library like mapstruct to do it.
        user.setUsername(userDTO.getUsername());
        user.setPassword(new BCryptPasswordEncoder(5).encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        user.setRoles("ROLE_USER");
        user.setEnabled(false);

        try {
            UserModel addedUser = userService.addUser(user);
            VerificationToken token = this.verificationTokenService.createVerificationToken(user.getUsername());
            String message = "Activate your account: " +
                    "http://localhost:8081/auth/activate?username=" + addedUser.getUsername() + "&code=" + token.getCode();
            emailService.sendSimpleMail(SensitiveData.USERNAME, message, "Confirm your email");
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateAccount(@RequestParam String username, @RequestParam UUID code){
        boolean isTokenValid = this.verificationTokenService.isTokenValid(username, code); // should be moved to filter
        if(isTokenValid){
            this.userService.activateUser(username);
            this.verificationTokenService.deleteTokenByCode(code);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}

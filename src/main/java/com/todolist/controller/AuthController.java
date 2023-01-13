package com.todolist.controller;

import com.todolist.entity.UserModel;
import com.todolist.service.UserDetailsServiceImpl;
import com.todolist.service.UserService;
import com.todolist.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/auth")
public class AuthController {


    private final UserService userService;


    public AuthController(UserService userService) {

        this.userService = userService;

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

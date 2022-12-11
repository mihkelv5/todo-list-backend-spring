package com.todolist.controller;

import com.todolist.constant.SecurityConstant;
import com.todolist.model.AuthenticationRequest;
import com.todolist.model.User;
import com.todolist.principal.UserPrincipalImpl;
import com.todolist.service.UserDetailsServiceImpl;
import com.todolist.service.UserService;
import com.todolist.util.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user){
        user.setId(null);
        User updatedUser = userService.updateUser(user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping("/search/all")
    public ResponseEntity<List<String>> userSearch(){
        List<String> matchingUsers = this.userService.findUsersByUsernameContains();
        return ResponseEntity.ok(matchingUsers);
    }


    @GetMapping("/event/{eventId}/all")
    public ResponseEntity<List<String>> userSearchNoEvent(@PathVariable Long eventId){
        List<String> matchingUsers = this.userService.userSearchNoEvent(eventId);
        return ResponseEntity.ok(matchingUsers);
    }

}

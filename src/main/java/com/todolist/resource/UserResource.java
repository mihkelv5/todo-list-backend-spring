package com.todolist.resource;

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
public class UserResource {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserService userService;
    private final JwtUtil jwtTokenUtil;

    public UserResource(AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService, UserService userService, JwtUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @RequestMapping("/hello")
    public String hello() {
        return "Hello, welcome";
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {

            throw new Exception("Incorrect username or password", e);
        }
        final UserPrincipalImpl userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        User user = userService.findUserByUsername(authenticationRequest.getUsername());
        user.setTasks(null);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(SecurityConstant.JWT_TOKEN_HEADER, jwt);
        return ResponseEntity.ok().headers(responseHeaders).body(user);
    }

    @PostMapping("/register")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        //user.setId(null);
        user.setPassword(new BCryptPasswordEncoder(5).encode(user.getPassword()));
        try {
            userService.addUser(user);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user){
        user.setId(null);
        User updatedUser = userService.updateUser(user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping("/search/all") //DEFINITELY must be changed to a POJO
    public ResponseEntity<List<String>> userSearch(){
        List<String> matchingUsers = this.userService.findUsersByUsernameContains();
        return ResponseEntity.ok(matchingUsers);
    }


    @GetMapping("/search/{eventId}") //DEFINITELY must be changed to a POJO
    public ResponseEntity<List<String>> userSearchNoEvent(@PathVariable Long eventId){
        List<String> matchingUsers = this.userService.userSearchNoEvent(eventId);
        return ResponseEntity.ok(matchingUsers);
    }

}

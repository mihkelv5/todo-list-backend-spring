package com.todolist.user;

import com.todolist.constant.SecurityConstant;
import com.todolist.models.AuthenticationRequest;
import com.todolist.util.JwtUtilOld;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/user")
public class UserResource {
    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService userDetailsService;
    private final JwtUtilOld jwtTokenUtil;

    public UserResource(AuthenticationManager authenticationManager, MyUserDetailsService userDetailsService, JwtUtilOld jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
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
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        final User user = userDetailsService.getUser(authenticationRequest.getUsername());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(SecurityConstant.JWT_TOKEN_HEADER, jwt);
        return ResponseEntity.ok().headers(responseHeaders).body(user);
    }

    @PostMapping("/register")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        user.setId(null);
        user.setPassword(new BCryptPasswordEncoder(5).encode(user.getPassword()));
        try {
            userDetailsService.addUser(user);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User user){
        user.setId(null);
        User updatedUser = userDetailsService.updateUser(user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }


}

package com.todolist.controller;


import com.todolist.model.UserModel;
import com.todolist.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PutMapping("/update")
    public ResponseEntity<UserModel> updateUser(@RequestBody UserModel user){
        user.setId(null);
        UserModel updatedUser = userService.updateUser(user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping("/event/{eventId}/all")
    @PreAuthorize("@preAuthFilter.checkIfUserInEvent(#eventId)")
    public ResponseEntity<List<String>> userSearchNoEvent(@PathVariable UUID eventId){
        List<String> matchingUsers = this.userService.userSearchNoEvent(eventId);
        return ResponseEntity.ok(matchingUsers);
    }

}

package com.todolist.controller;


import com.todolist.entity.Friendship;
import com.todolist.entity.UserModel;
import com.todolist.entity.dto.FriendshipDTO;
import com.todolist.service.FriendshipService;
import com.todolist.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping(path = "/api/user")
public class UserController {

    private final UserService userService;
    private final FriendshipService friendshipService;

    public UserController(UserService userService, FriendshipService friendshipService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
    }


    @PutMapping("/update")
    public ResponseEntity<UserModel> updateUser(@RequestBody UserModel user){
        user.setId(null);
        UserModel updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/notIn/event/{eventId}")
    @PreAuthorize("@preAuthMethodFilter.checkIfUserInEvent(#eventId)")
    public ResponseEntity<List<String>> userSearchNoEvent(@PathVariable UUID eventId){
        List<String> matchingUsers = this.userService.findUsersNotInEvent(eventId);
        return ResponseEntity.ok(matchingUsers);
    }


    @GetMapping("/event/{eventId}")
    @PreAuthorize("@preAuthMethodFilter.checkIfUserInEvent(#eventId)")
    public ResponseEntity<List<UserModel>> findUsersByEventId(@PathVariable("eventId") UUID eventId){
        List<UserModel> users = this.userService.findUsersByEventId(eventId);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/friends/all")
    public ResponseEntity<?> getFriends(){
        Set<FriendshipDTO> friendships = this.friendshipService.findCurrentUserFriends();
        return ResponseEntity.ok(friendships);
    }
    @PostMapping("/friends/add")
    public ResponseEntity<?> addFriend(@RequestParam String username){
        Friendship friendship = this.friendshipService.addFriend(username);
        return ResponseEntity.ok(friendship);
    }



    @PutMapping("/friends/request")
    public ResponseEntity<?> acceptFriend(@RequestParam String username, @RequestParam boolean accepted, @RequestParam boolean blocked) {
        try {
            Friendship friendship = this.friendshipService.changeFriendRequestStatus(username, accepted, blocked);
            if (friendship == null) {
                Map<String, String> response = new HashMap<>();
                response.put("response", "friend request deleted");
                return ResponseEntity.ok(response);
            } else if (friendship.isAccepted() || friendship.isBlocked()) {
                return ResponseEntity.ok(friendship);
            }
        } catch (ResponseStatusException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.badRequest().body("Something went wrong");

    }

    @Transactional
    @DeleteMapping("/friends/delete")
    public ResponseEntity<?> deleteFriend(@RequestParam String username){
        try{
            this.friendshipService.removeFriend(username);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }


}

package com.todolist.controller;


import com.todolist.entity.user.Friendship;
import com.todolist.entity.user.UserModel;
import com.todolist.entity.dto.FriendshipDTO;
import com.todolist.entity.dto.PrivateUserDTO;
import com.todolist.entity.dto.PublicUserDTO;
import com.todolist.service.FriendshipService;
import com.todolist.service.ProfilePictureService;
import com.todolist.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping(path = "/api/user")
public class UserController {

    private final UserService userService;
    private final FriendshipService friendshipService;

    private final ProfilePictureService profilePictureService;

    public UserController(UserService userService, FriendshipService friendshipService, ProfilePictureService profilePictureService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.profilePictureService = profilePictureService;
    }

    @GetMapping("/me")
    public ResponseEntity<PrivateUserDTO> getUserData (){
        UserModel user = this.userService.getCurrentUser();
        PrivateUserDTO userDTO = PrivateUserDTO.privateUserDTOConverter(user);
        userDTO.setImageString(profilePictureService.getUserImage(user.getUsername()));
        return ResponseEntity.ok(userDTO);
    }


    @PutMapping("/update")
    public ResponseEntity<UserModel> updateUser(@RequestBody UserModel user){
        user.setId(null);
        UserModel updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/notIn/event/{eventId}")
    @PreAuthorize("@preAuthMethodFilter.checkIfUserInEvent(#eventId)")
    public ResponseEntity<Set<PublicUserDTO>> userSearchNoEvent(@PathVariable UUID eventId){
        Set<PublicUserDTO> matchingUsers = this.userService.findUsersNotInEvent(eventId);
        matchingUsers.forEach(user -> user.setImageString(this.profilePictureService.getUserImage(user.getUsername())));
        return ResponseEntity.ok(matchingUsers);
    }


    @GetMapping("/event/{eventId}")
    @PreAuthorize("@preAuthMethodFilter.checkIfUserInEvent(#eventId)")
    public ResponseEntity<Set<PublicUserDTO>> findUsersByEventId(@PathVariable("eventId") UUID eventId){
        Set<PublicUserDTO> users = this.userService.findUsersByEventId(eventId);
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

    @PostMapping("/profile/picture")
    public ResponseEntity<?> uploadImage(@RequestParam("image")MultipartFile image) {
        try {

            String message = this.profilePictureService.uploadImageToServer(image);
            Map<String, String> response = new HashMap<>();
            response.put("response", message);
            return ResponseEntity.ok(response);
        } catch (IOException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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

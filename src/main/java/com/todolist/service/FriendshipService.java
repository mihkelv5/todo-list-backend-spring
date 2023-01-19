package com.todolist.service;

import com.todolist.entity.Friendship;
import com.todolist.entity.UserModel;
import com.todolist.entity.dto.FriendshipDTO;
import com.todolist.repository.FriendshipRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final UserService userService;

    public FriendshipService(FriendshipRepository friendshipRepository, UserService userService) {
        this.friendshipRepository = friendshipRepository;
        this.userService = userService;
    }

    public Set<FriendshipDTO> findCurrentUserFriends(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        //probably could be done in one query with custom queries, JPA did not allow me to use UNION with selects
        Set<Friendship> friends = new HashSet<>(this.friendshipRepository.findAllFriendsWhereUser(username));
        //friends.addAll(this.friendshipRepository.findAllFriendsWhereFriend(username));
        return friends.stream().map(friendship -> FriendshipDTO.friendshipDTOConverter(friendship, username)).collect(Collectors.toSet());
    }

    public Friendship addFriend(String invitedUsername){
        UserModel currentUser = this.userService.getCurrentUser();
        UserModel invitedUser = this.userService.findUserByUsername(invitedUsername);
        Friendship friendship;

        if(invitedUser == null) {
            throw new RuntimeException("Username \""+ invitedUsername + "\" not found"); //create custom exceptions.
        }

        //check if they are already friends or sent requests
        if((friendship = this.friendshipRepository.findFriendship(currentUser, invitedUser)) != null) {
            if(friendship.isAccepted()){
                throw new RuntimeException("You are already friends with user: " + invitedUsername);
            } else {
                throw new RuntimeException("You have already sent an invite to user: " + invitedUsername);
            } //if user has not responded to invite or user has blocked the other user. ISSUE: if both users want to send invites to each other, then error message might be confusing.
        }

        friendship = new Friendship();
        friendship.setUser(currentUser);
        friendship.setFriend(invitedUser);

        return this.friendshipRepository.save(friendship);
    }




    public Friendship acceptFriend(String username) {
        UserModel currentUser = this.userService.getCurrentUser();
        UserModel acceptedUser = this.userService.findUserByUsername(username);
        if(acceptedUser == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Username \""+ username + "\" not found"); //create custom exceptions.
        }
        Friendship friendship = this.friendshipRepository.findFriendshipRequest(currentUser, acceptedUser);
        friendship.setAccepted(true);
        return this.friendshipRepository.save(friendship);
    }


    public Friendship changeFriendRequestStatus(String username, boolean accepted, boolean blocked) {
        UserModel currentUser = this.userService.getCurrentUser();
        UserModel acceptedUser = this.userService.findUserByUsername(username);
        if(acceptedUser == null) {
            System.out.println("no user found");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username \""+ username + "\" not found"); //create custom exceptions.
        }
        Friendship friendship = this.friendshipRepository.findFriendshipRequest(currentUser, acceptedUser);
        if(friendship == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You have no pending requests from user: " + username);
        }
        if(accepted) {
            friendship.setAccepted(true);
            return this.friendshipRepository.save(friendship);
        } else if (blocked) {
            friendship.setBlocked(true);
            return this.friendshipRepository.save(friendship);
        } else {
            this.friendshipRepository.delete(friendship);
            return null;
        }
    }


    @Transactional
    public void removeFriend(String username) {
        UserModel currentUser = this.userService.getCurrentUser();
        UserModel removedUser = this.userService.findUserByUsername(username);
        if(removedUser == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username \""+ username + "\" not found"); //create custom exceptions.
        }
        Friendship friendship = this.friendshipRepository.findFriendship(currentUser, removedUser);
        this.friendshipRepository.delete(friendship);
    }
}

package com.todolist.service;

import com.todolist.model.UserModel;
import com.todolist.security.userdetails.UserDetailsImpl;
import com.todolist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //POST method

    public void addUser(UserModel user){
        userRepository.save(user);
    }

    //GET methods
    public UserModel findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Set<UserModel> findAllUsersByUsernames(Set<String> usernames) {
        return this.userRepository.findAllUsersByUsernameSet(usernames);
    }



    public List<String> findUsersNotInEvent(UUID eventId) {
        return this.userRepository.findUsersNotInEvent(eventId);
    }

    public List<UserModel> findUsersByEventId(UUID eventId) {
        return this.userRepository.findUsersByEventsId(eventId);
    }

    public boolean isUsernameInEvent(String username, UUID eventId){
        return this.userRepository.existsUserModelByUsernameAndEventsId(username, eventId);
    }



    //UPDATE method
    public UserModel updateUser(UserModel user){
        return userRepository.save(user);
    }

    //helper methods
    public UserModel getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Optional<UserModel> user = userRepository.findById(userDetails.getId());
        return user.orElseGet(UserModel::new);
    }

}

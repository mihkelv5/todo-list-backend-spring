package com.todolist.service;

import com.todolist.entity.UserModel;
import com.todolist.entity.dto.UserCreationDTO;
import com.todolist.security.userdetails.UserDetailsImpl;
import com.todolist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //POST method

    public UserModel addUser(UserCreationDTO userDTO){
        if(userRepository.existsByUsername(userDTO.getUsername())){
            throw new DuplicateKeyException("Username already taken");
        }
        if(userRepository.existsByEmail(userDTO.getEmail())){
            throw new DuplicateKeyException("Email already taken");
        }

        UserModel user = new UserModel();
        user.setUsername(userDTO.getUsername());
        user.setPassword(new BCryptPasswordEncoder(5).encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        user.setRoles("ROLE_USER");
        user.setEnabled(false);

        return userRepository.save(user);
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

    public void activateUser(String username){
        UserModel user = this.userRepository.findByUsername(username);
        user.setEnabled(true);
        this.userRepository.save(user);
    }

    //helper methods
    public UserModel getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        Optional<UserModel> user = userRepository.findById(userDetails.getId());
        return user.orElseGet(UserModel::new);
    }

}

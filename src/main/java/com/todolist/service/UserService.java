package com.todolist.service;

import com.todolist.entity.UserModel;
import com.todolist.entity.dto.PublicUserDTO;
import com.todolist.entity.dto.UserCreationDTO;
import com.todolist.security.userdetails.UserDetailsImpl;
import com.todolist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.CredentialNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //POST method

    public UserModel addUser(UserCreationDTO userDTO) throws CredentialNotFoundException {
        if(userDTO.getUsername() == null || userDTO.getEmail() == null || userDTO.getPassword() == null){
            throw new CredentialNotFoundException("User must have username, email and password");
        }

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

    public Set<PublicUserDTO> findUsersNotInEvent(UUID eventId) {
        return this.userRepository.findUsersNotInEvent(eventId).stream().map(PublicUserDTO::publicUserDTOConverter).collect(Collectors.toSet());
    }

    public Set<PublicUserDTO> findUsersByEventId(UUID eventId) {
        Set<UserModel> eventUsers = this.userRepository.findUsersByEventsId(eventId);
        return eventUsers.stream().map(PublicUserDTO::publicUserDTOConverter).collect(Collectors.toSet());
    }

    public boolean isUsernameInEvent(String username, UUID eventId){
        return this.userRepository.existsUserModelByUsernameAndEventsId(username, eventId);
    }

    public boolean isUserIdInEvent(UUID userId, UUID eventId){
        return this.userRepository.existsUserModelByIdAndEventsId(userId, eventId);
    }

    public Set<PublicUserDTO> getInvitedUsers(UUID eventId){
        Set<UserModel> userModels = this.userRepository.findUserModelsFromEventInvitationWithEventId(eventId);
        return userModels.stream().map(PublicUserDTO::publicUserDTOConverter).collect(Collectors.toSet());
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

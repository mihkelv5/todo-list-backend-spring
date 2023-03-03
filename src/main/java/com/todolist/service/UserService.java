package com.todolist.service;

import com.todolist.entity.dto.PrivateUserDTO;
import com.todolist.entity.user.UserModel;
import com.todolist.entity.dto.PublicUserDTO;
import com.todolist.entity.dto.UserCreationDTO;
import com.todolist.repository.EventRepository;
import com.todolist.repository.TaskRepository;
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

@Service
public class UserService {
    private final UserRepository userRepository;

    private final TaskRepository taskRepository;
    private final EventRepository eventRepository;

    private final ProfilePictureService profilePictureService;

    @Autowired
    public UserService(UserRepository userRepository, TaskRepository taskRepository, EventRepository eventRepository, ProfilePictureService profilePictureService) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.eventRepository = eventRepository;
        this.profilePictureService = profilePictureService;
    }

    //POST method

    public UserModel addUser(UserCreationDTO userDTO) throws CredentialNotFoundException {
        if(userDTO.getUsername() == null || userDTO.getEmail() == null || userDTO.getPassword() == null){
            throw new CredentialNotFoundException("User must have username, email and password");
        }

        if(userRepository.existsUserByUsername(userDTO.getUsername())){
            throw new DuplicateKeyException("Username already taken");
        }
        if(userRepository.existsByUserEmail(userDTO.getEmail())){
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
        return userRepository.findUserByUsername(username);
    }

    public Set<UserModel> findAllUsersByUsernames(Set<String> usernames) {
        return this.userRepository.findAllUsersByUsernameSet(usernames);
    }

    public Set<PublicUserDTO> findUsersNotInEvent(UUID eventId) {
        Set<UserModel> usersNotInEventByEventId = this.userRepository.findUsersNotInEventByEventId(eventId);
        return this.publicUserDTOSetConverter(usersNotInEventByEventId);
    }

    public Set<PublicUserDTO> findUsersByEventId(UUID eventId) {
        Set<UserModel> eventUsers = this.userRepository.findUsersByEventsId(eventId);
        return this.publicUserDTOSetConverter(eventUsers);
    }

    public boolean isUsernameInEvent(String username, UUID eventId){
        return this.userRepository.existsUserModelByUsernameAndEventsId(username, eventId);
    }

    public boolean isUserIdInEvent(UUID userId, UUID eventId){
        return this.userRepository.existsUserModelByIdAndEventsId(userId, eventId);
    }

    public Set<PublicUserDTO> getInvitedUsers(UUID eventId){
        Set<UserModel> userModels = this.userRepository.findAlreadyInvitedUsersByEventId(eventId);
        return this.publicUserDTOSetConverter(userModels);
    }



    //UPDATE method
    public UserModel updateUser(UserModel user){
        return userRepository.save(user);
    }

    public void activateUser(String username){
        UserModel user = this.userRepository.findUserByUsername(username);
        user.setEnabled(true);
        this.userRepository.save(user);
    }

    //helper methods
    public UserModel getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        return userRepository.findUserById(userDetails.getId());
    }

    public PublicUserDTO publicUserDTOConverter(UserModel userModel) {
        PublicUserDTO publicUserDTO = new PublicUserDTO();
        publicUserDTO.setUsername(userModel.getUsername());
        publicUserDTO.setJoinDate(userModel.getJoinDate());
        publicUserDTO.setImageString(profilePictureService.getUserImage(userModel.getUsername()));

        publicUserDTO.setTasksCreated(this.taskRepository.countTaskModelByOwnerUser(userModel.getUsername()));
        publicUserDTO.setTasksCompleted(this.taskRepository.countTaskModelByAssignedUserAndComplete(userModel));
        publicUserDTO.setActiveTasks(this.taskRepository.countTaskModelByAssignedUserAndNotComplete(userModel));
        publicUserDTO.setGroupsJoined(this.eventRepository.countEventModelsByUser(userModel));

        return publicUserDTO;
    }

    public Set<PublicUserDTO> publicUserDTOSetConverter(Set<UserModel> users){
        Set<PublicUserDTO> publicUserDTOSet = new HashSet<>();
        users.forEach(user -> publicUserDTOSet.add(this.publicUserDTOConverter(user)));
        return publicUserDTOSet;
    }

    public PrivateUserDTO privateUserDTOConverter(UserModel userModel){
        PrivateUserDTO privateUserDTO = new PrivateUserDTO();
        privateUserDTO.setUserId(userModel.getId());
        privateUserDTO.setUsername(userModel.getUsername());
        privateUserDTO.setEmail(userModel.getEmail());
        privateUserDTO.setJoinDate(userModel.getJoinDate());
        privateUserDTO.setImageString(profilePictureService.getUserImage(userModel.getUsername()));

        privateUserDTO.setTasksCreated(this.taskRepository.countTaskModelByOwnerUser(userModel.getUsername()));
        privateUserDTO.setTasksCompleted(this.taskRepository.countTaskModelByAssignedUserAndComplete(userModel));
        privateUserDTO.setActiveTasks(this.taskRepository.countTaskModelByAssignedUserAndNotComplete(userModel));
        privateUserDTO.setGroupsJoined(this.eventRepository.countEventModelsByUser(userModel));

        return privateUserDTO;
    }

}

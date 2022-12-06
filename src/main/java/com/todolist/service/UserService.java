package com.todolist.service;

import com.todolist.model.Event;
import com.todolist.model.User;
import com.todolist.principal.UserPrincipalImpl;
import com.todolist.repository.EventRepository;
import com.todolist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;


    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    public void addUser(User user){
        userRepository.save(user);
    }

    public User updateUser(User user){
        return userRepository.save(user);
    }

    public User findUserById(long id){
        return userRepository.findUserById(id);
    }

    public List<User> findUsersByEvent(Event event) {
        return userRepository.findUsersByEvents(event);
    }


    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipalImpl userDetails = (UserPrincipalImpl) auth.getPrincipal();
        Optional<User> user = userRepository.findById(userDetails.getId());
        return user.orElseGet(User::new);
    }

    public boolean isUserInEvent(String username, Long eventId){
        return userRepository.existsUserByEventsIdAndUsername(eventId, username);
    }

    public boolean isUserInEventId(Long userId, Long eventId){ //dummy method for testing

        return userRepository.existsUserByEventsIdAndId(eventId, userId);
    }
}

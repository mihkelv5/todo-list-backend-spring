package com.todolist.service;

import com.todolist.model.Event;
import com.todolist.model.User;
import com.todolist.principal.UserPrincipalImpl;
import com.todolist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipalImpl userDetails = (UserPrincipalImpl) auth.getPrincipal();
        Optional<User> user = userRepository.findById(userDetails.getId());
        return user.orElseGet(User::new);
    }

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Set<User> findAllUsersByUsernameSet(Set<String> usernames) {
        return this.userRepository.findAllUsersByUsernameSet(usernames);
    }

    public void addUser(User user){
        userRepository.save(user);
    }

    public User updateUser(User user){
        return userRepository.save(user);
    }

    public List<User> findUsersByEvent(Event event) {
        return userRepository.findUsersByEvents(event);
    }


    public boolean isUserInEvent(Long eventId){
        String username = this.getCurrentUser().getUsername();
        return userRepository.existsUserByEventsIdAndUsername(eventId, username);
    }

    public List<String> userSearchNoEvent(Long eventId) {
        return this.userRepository.findUsersNotInEvent(eventId);
    }
}

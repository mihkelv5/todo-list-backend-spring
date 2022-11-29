package com.todolist.service;

import com.todolist.model.User;
import com.todolist.principal.MyUserPrincipal;
import com.todolist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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


    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserPrincipal userDetails = (MyUserPrincipal) auth.getPrincipal();
        Optional<User> user = userRepository.findById(userDetails.getId());
        return user.orElseGet(User::new);
    }
}

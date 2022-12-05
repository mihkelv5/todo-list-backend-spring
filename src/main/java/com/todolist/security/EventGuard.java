package com.todolist.security;

import com.todolist.model.User;
import com.todolist.service.EventService;
import com.todolist.service.UserService;
import com.todolist.util.JwtUtil;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;


@Component
public class EventGuard  {

    private final UserService userService;

    public EventGuard(UserService userService) {

        this.userService = userService;

    }

    public boolean isUserInEvent(Authentication authentication, Long eventId){
        User user = userService.getCurrentUser();
        System.out.println(eventId);
        return userService.isUserInEvent(user, eventId);
    }

}

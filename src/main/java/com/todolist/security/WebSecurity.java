package com.todolist.security;

import com.todolist.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


@Component
public class WebSecurity {

    UserService userService;

    public WebSecurity(UserService userService) {
        this.userService = userService;
    }

    public boolean checkIfUserInEvent(Authentication authentication, String eventId) {
        if(eventId.matches("[0-9]+")){
            return userService.isUserInEvent(authentication.getName(), Long.valueOf(eventId));
        }
        return false;
    }


}

package com.todolist.security;

import com.todolist.service.EventInvitationService;
import com.todolist.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


@Component

public class WebSecurity {

    UserService userService;
    EventInvitationService invitationService;

    public WebSecurity(UserService userService, EventInvitationService invitationService) {
        this.userService = userService;
        this.invitationService = invitationService;
    }

    public boolean checkIfUserInEvent(Authentication authentication, String eventId) {
        if(eventId.matches("[0-9]+")){
            return userService.isUserInEvent(authentication.getName(), Long.valueOf(eventId));
        }
        return false;
    }


    public boolean checkIfUserIsInvited(Authentication authentication, String invitationId) {
        System.out.println("test");
        return this.invitationService.isInvitationValid(authentication.getName(), Long.valueOf(invitationId));

    }
}

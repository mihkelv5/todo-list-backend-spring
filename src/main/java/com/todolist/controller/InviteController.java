package com.todolist.controller;

import com.todolist.model.EventInvitation;
import com.todolist.model.User;
import com.todolist.service.EventInvitationService;
import com.todolist.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;


@RestController
@RequestMapping(path = "/api/invite")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class InviteController {


    private final EventInvitationService eventInvitationService;
    private final UserService userService;

    public InviteController(EventInvitationService eventInvitationService, UserService userService) {

        this.eventInvitationService = eventInvitationService;
        this.userService = userService;
    }

    @PostMapping("/event/{eventId}/user/{username}")
    public ResponseEntity<?> inviteUserToEvent(@PathVariable("eventId") Long eventId, @PathVariable("username") String username) {
        return this.eventInvitationService.inviteUserToEvent(eventId, username);
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<EventInvitation>> getUserEventInvitations(){
        User user = this.userService.getCurrentUser();
        List<EventInvitation> eventInvitationList = this.eventInvitationService.findUserInvitations(user);
        return ResponseEntity.ok(eventInvitationList);
    }

    //preauthorize with currentUser
    @Transactional
    @PutMapping("/accept/{invitationId}")
    @PreAuthorize("@webSecurity.checkIfUserIsInvited(authentication, #invitationId)")
    public ResponseEntity<?> acceptEventInvitation(@PathVariable ("invitationId") Long invitationId) {
        return this.eventInvitationService.acceptInvite(invitationId);
    }
    @Transactional
    @DeleteMapping("/decline/{invitationId}")
    @PreAuthorize("@webSecurity.checkIfUserIsInvited(authentication, #invitationId)")
    public ResponseEntity<?> declineEventInvitation(@PathVariable("invitationId") Long invitationId){
        return this.eventInvitationService.declineInvite(invitationId);
    }
}

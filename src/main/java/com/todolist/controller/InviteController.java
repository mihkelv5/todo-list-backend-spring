package com.todolist.controller;

import com.todolist.model.EventInvitationModel;
import com.todolist.model.UserModel;
import com.todolist.service.EventInvitationService;
import com.todolist.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping(path = "/api/invite")
public class InviteController {


    private final EventInvitationService eventInvitationService;
    private final UserService userService;

    public InviteController(EventInvitationService eventInvitationService, UserService userService) {

        this.eventInvitationService = eventInvitationService;
        this.userService = userService;
    }

    @PostMapping("/event/{eventId}/user/{username}")
    @PreAuthorize("@preAuthFilter.checkIfUserInEvent(#eventId)")
    public ResponseEntity<?> inviteUserToEvent(@PathVariable("eventId") UUID eventId, @PathVariable("username") String username) {
        return this.eventInvitationService.inviteUserToEvent(eventId, username);
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<EventInvitationModel>> getUserEventInvitations(){
        UserModel user = this.userService.getCurrentUser();
        List<EventInvitationModel> eventInvitationList = this.eventInvitationService.findUserInvitations(user);
        return ResponseEntity.ok(eventInvitationList);
    }

    //preauthorize with currentUser
    @Transactional
    @PutMapping("/accept/{invitationId}")
    @PreAuthorize("@preAuthFilter.checkIfUserIsInvited(#invitationId)")
    public ResponseEntity<?> acceptEventInvitation(@PathVariable ("invitationId") UUID invitationId) {
        return this.eventInvitationService.acceptInvite(invitationId);
    }
    @Transactional
    @DeleteMapping("/decline/{invitationId}")
    @PreAuthorize("@preAuthFilter.checkIfUserIsInvited(#invitationId)")
    public ResponseEntity<?> declineEventInvitation(@PathVariable("invitationId") UUID invitationId){
        return this.eventInvitationService.declineInvite(invitationId);
    }
}

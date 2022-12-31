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

    public InviteController(EventInvitationService eventInvitationService) {
        this.eventInvitationService = eventInvitationService;
    }


    @PostMapping("/event/{eventId}/user/{username}")
    @PreAuthorize("@preAuthFilter.checkIfUserInEvent(#eventId)")
    public ResponseEntity<?> inviteUserToEvent(@PathVariable("eventId") UUID eventId, @PathVariable("username") String username) {
        boolean success = this.eventInvitationService.inviteUserToEvent(eventId, username);
        if(success){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<EventInvitationModel>> getUserEventInvitations(){
        List<EventInvitationModel> eventInvitationList = this.eventInvitationService.findUserInvitations();
        return ResponseEntity.ok(eventInvitationList);
    }

    @Transactional
    @PutMapping("/accept/{invitationId}")
    @PreAuthorize("@preAuthFilter.checkIfUserIsInvited(#invitationId)")
    public ResponseEntity<?> acceptEventInvitation(@PathVariable ("invitationId") UUID invitationId) {
        boolean success = this.eventInvitationService.acceptInvite(invitationId);
        if(success){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }


    @Transactional
    @DeleteMapping("/decline/{invitationId}")
    @PreAuthorize("@preAuthFilter.checkIfUserIsInvited(#invitationId)")
    public ResponseEntity<?> declineEventInvitation(@PathVariable("invitationId") UUID invitationId){
        boolean success = this.eventInvitationService.declineInvite(invitationId);
        if(success) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}

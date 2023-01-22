package com.todolist.controller;

import com.todolist.entity.EventInvitationModel;
import com.todolist.entity.dto.PublicUserDTO;
import com.todolist.service.EventInvitationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.transaction.Transactional;

import java.util.*;


@RestController
@RequestMapping(path = "/api/invite")
public class InviteController {


    private final EventInvitationService eventInvitationService;

    public InviteController(EventInvitationService eventInvitationService) {
        this.eventInvitationService = eventInvitationService;
    }


    @PostMapping("/event/{eventId}")
    @PreAuthorize("@preAuthMethodFilter.checkIfUserInEvent(#eventId)")
    public ResponseEntity<?> inviteUserToEvent(@PathVariable("eventId") UUID eventId, @RequestBody Set<String> usernames) {
        Set<PublicUserDTO> invitedUsers = this.eventInvitationService.inviteUserToEvent(eventId, usernames);
        if(!invitedUsers.isEmpty()){
            return ResponseEntity.ok(invitedUsers);
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
    @PreAuthorize("@preAuthMethodFilter.checkIfUserIsInvited(#invitationId)")
    public ResponseEntity<?> acceptEventInvitation(@PathVariable ("invitationId") UUID invitationId) {
        boolean success = this.eventInvitationService.acceptInvite(invitationId);
        if(success){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }


    @Transactional
    @DeleteMapping("/decline/{invitationId}")
    @PreAuthorize("@preAuthMethodFilter.checkIfUserIsInvited(#invitationId)")
    public ResponseEntity<?> declineEventInvitation(@PathVariable("invitationId") UUID invitationId){
        boolean success = this.eventInvitationService.declineInvite(invitationId);
        if(success) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @Transactional
    @DeleteMapping("/delete/{eventId}/user/{username}")
    @PreAuthorize("@preAuthMethodFilter.checkIfCurrentUserIsInEvent(#eventId)")
    public ResponseEntity<?> deleteEventInvitation(@PathVariable UUID eventId, @PathVariable String username){
        this.eventInvitationService.deleteInvite(username, eventId);
        Map<String, String> response = new HashMap<>();
        response.put("response", "deleted user: " + username);
        return ResponseEntity.ok(response);

    }
}

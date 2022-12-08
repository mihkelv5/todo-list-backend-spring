package com.todolist.resource;

import com.todolist.model.EventInvitation;
import com.todolist.service.EventInvitationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;


@RestController
@RequestMapping(path = "/api/invite")
public class InviteResource {


    private final EventInvitationService eventInvitationService;

    public InviteResource(EventInvitationService eventInvitationService) {

        this.eventInvitationService = eventInvitationService;
    }

    @PostMapping("/event/{eventId}/user/{username}")
    public ResponseEntity<?> inviteUserToEvent(@PathVariable("eventId") Long eventId, @PathVariable("username") String username) {
        return this.eventInvitationService.inviteUserToEvent(eventId, username);
    }

    @GetMapping("/{username}/get/all") //TODO: make filter so only invited user can see the invitations
    public ResponseEntity<List<EventInvitation>> getUserEventInvitations(@PathVariable("username") String username){
        List<EventInvitation> eventInvitationList = this.eventInvitationService.findUserInvitations(username);
        return ResponseEntity.ok(eventInvitationList);
    }
    @Transactional
    @PutMapping("/accept/{invitationId}")
    public ResponseEntity<?> acceptEventInvitation(@PathVariable ("invitationId") Long invitationId) {
        return this.eventInvitationService.acceptInvite(invitationId);
    }
    @Transactional
    @DeleteMapping("/decline/{invitationId}")
    public ResponseEntity<?> declineEventInvitation(@PathVariable("invitationId") Long invitationId){
        return this.eventInvitationService.declineInvite(invitationId);
    }
}

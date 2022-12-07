package com.todolist.service;

import com.todolist.model.Event;
import com.todolist.model.EventInvitation;
import com.todolist.model.User;
import com.todolist.repository.EventInvitationRepository;
import com.todolist.repository.EventRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
public class EventInvitationService {

    private final EventInvitationRepository eventInvitationRepository;
    private final EventRepository eventRepository;
    private final UserService userService;
    private final EventService eventService;

    public EventInvitationService(EventInvitationRepository eventInvitationRepository, UserService userService, EventRepository eventRepository, EventService eventService) {
        this.eventInvitationRepository = eventInvitationRepository;
        this.userService = userService;
        this.eventRepository = eventRepository;
        this.eventService = eventService;
    }

    public List<EventInvitation> findUserInvitations (String username) {
        User user = this.userService.findUserByUsername(username);
       //return this.eventInvitationRepository.findAllEventInvitationsByIsAccepted(false);
        return this.eventInvitationRepository.findAllByInvitedUserAndExpirationDateIsAfterAndIsAccepted(user, new Date(), false);
    }


    public ResponseEntity<?> inviteUserToEvent (Long eventId, String username){
        Event event = this.eventService.findEventById(eventId);
        User invitedUser = this.userService.findUserByUsername(username);
        User requester = this.userService.getCurrentUser();
        if(Objects.equals(username, requester.getUsername())) {
            return ResponseEntity.badRequest().body("You cannot invite yourself");
        }
        if(this.userService.isUserInEvent(username, eventId)){
            return ResponseEntity.badRequest().body("User " + username + " is already joined the event!");
        }
        if(this.eventInvitationRepository.existsByInvitedUserAndEventIdAndExpirationDateIsAfter(invitedUser, eventId, new Date())){
            return ResponseEntity.badRequest().body("User " + username + "is already invited to the event!");
        }
        EventInvitation invitation = new EventInvitation();
        invitation.setRequesterUsername(requester.getUsername());
        invitation.setInvitedUser(invitedUser);
        invitation.setEventId(eventId);
        invitation.setEventName(event.getTitle());
        this.eventInvitationRepository.save(invitation);
        HashMap<String, String> map = new HashMap<>();
        map.put("success", "User " + username + " invited to event!");
        return  ResponseEntity.ok().body(map);
    }

    public ResponseEntity<?> acceptInvite(Long invitationId){ //TODO: ask if this is a good practice
        EventInvitation invitation = this.eventInvitationRepository.findEventInvitationByIdAndExpirationDateIsAfter(invitationId, new Date());
        User user = this.userService.getCurrentUser();

        if(invitation == null ) {
            return ResponseEntity.badRequest().body("Invitation not found or it has been expired");
        }

        if(!Objects.equals(user.getId(), invitation.getInvitedUser().getId())) {
            return ResponseEntity.badRequest().body("You cannot accept other users invitation");
            //shouldn't occur unless someone sends data straight to backend, and should be filtered before?
        }
        //TODO: delete invitation/set state to accepted
        Event event = this.eventService.saveUserToEvent(invitation.getEventId(), invitation.getInvitedUser().getUsername());
        if(event != null){
            invitation.accept();
            this.eventInvitationRepository.save(invitation);
            return ResponseEntity.ok(event);
        }
        return ResponseEntity.unprocessableEntity().body("Something went wrong"); //should be 500 code? Do I want to show it to client?
    }

    @Transactional
    public ResponseEntity<?> declineInvite(Long invitationId){
        EventInvitation invitation = this.eventInvitationRepository.findEventInvitationById(invitationId);
        User user = this.userService.getCurrentUser();
        if(!Objects.equals(user.getUsername(), invitation.getInvitedUser().getUsername())) {
            return ResponseEntity.badRequest().body("You cannot decline other users invitation");
            //shouldn't occur unless someone sends data straight to backend, and should be filtered before?
        }
        this.eventInvitationRepository.deleteEventInvitationById(invitationId);
        return ResponseEntity.ok().body("Request deleted");
    }

}

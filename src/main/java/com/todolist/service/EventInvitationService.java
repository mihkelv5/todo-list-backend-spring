package com.todolist.service;

import com.todolist.model.Event;
import com.todolist.model.EventInvitation;
import com.todolist.model.User;
import com.todolist.repository.EventInvitationRepository;
import com.todolist.repository.EventRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class EventInvitationService {

    EventInvitationRepository eventInvitationRepository;
    EventRepository eventRepository;
    UserService userService;
    private EventService eventService;

    public EventInvitationService(EventInvitationRepository eventInvitationRepository, UserService userService, EventRepository eventRepository, EventService eventService) {
        this.eventInvitationRepository = eventInvitationRepository;
        this.userService = userService;
        this.eventRepository = eventRepository;
        this.eventService = eventService;
    }

    public List<EventInvitation> findUserInvitations (Long userId) {
        User user = this.userService.findUserById(userId);
       //return this.eventInvitationRepository.findAllEventInvitationsByIsAccepted(false);
        return this.eventInvitationRepository.findAllByInvitedUserAndExpirationDateIsAfterAndIsAccepted(user, new Date(), false);
    }

    public boolean inviteUserToEvent(Long eventId, Long userId) {
        User user = this.userService.getCurrentUser();
        User invitedUser = this.userService.findUserById(userId);

        if(Objects.equals(userId, user.getId())) return false; //users cannot invite themselves

        if(!this.userService.isUserInEvent(invitedUser.getUsername(), eventId)
        && this.userService.isUserInEvent(user.getUsername(), eventId)){
            User requester = this.userService.getCurrentUser();
            EventInvitation invitation = new EventInvitation();
            invitation.setRequesterUsername(requester.getUsername());
            invitation.setInvitedUser(invitedUser);
            invitation.setEventId(eventId);
            this.eventInvitationRepository.save(invitation);
            return true;
        }
        return false;
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
        Event event = this.eventService.saveUserToEvent(invitation.getEventId(), invitation.getInvitedUser().getId());
        if(event != null){
            invitation.accept();
            this.eventInvitationRepository.save(invitation);
            return ResponseEntity.ok(event);
        }
        return ResponseEntity.unprocessableEntity().body("Something went wrong"); //should be 500 code? Do I want to show it to client?

    }


}

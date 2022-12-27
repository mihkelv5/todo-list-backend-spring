
package com.todolist.service;


import com.todolist.model.Event;
import com.todolist.model.EventInvitation;
import com.todolist.model.User;
import com.todolist.repository.EventInvitationRepository;
import com.todolist.repository.EventRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.List;



@Service
public class EventInvitationService {

    private final EventInvitationRepository eventInvitationRepository;
    private final EventRepository eventRepository;
    private final EventService eventService;
    private final UserService userService;

    public EventInvitationService(EventInvitationRepository eventInvitationRepository, UserService userService, EventRepository eventRepository, EventService eventService) {
        this.eventInvitationRepository = eventInvitationRepository;
        this.eventRepository = eventRepository;
        this.eventService = eventService;
        this.userService = userService;
    }

    public List<EventInvitation> findUserInvitations (User user) {
        return this.eventInvitationRepository.findAllByInvitedUserAndExpirationDateIsAfterAndIsAccepted(user, new Date(), false);
    }




    public ResponseEntity<?> inviteUserToEvent (Long eventId, String username){
        Event event = this.eventRepository.findEventById(eventId);
        User invitedUser = this.userService.findUserByUsername(username);
        User requester = this.userService.getCurrentUser();
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

    @Transactional
    public ResponseEntity<?> acceptInvite(Long invitationId){ //This should return boolean instead of responseEntity, was just for test
        EventInvitation invitation = this.eventInvitationRepository.findEventInvitationByIdAndExpirationDateIsAfter(invitationId, new Date());

        Event event = this.eventService.saveUserToEvent(invitation.getEventId(), invitation.getInvitedUser().getUsername());

        //if everything goes correctly
        if(event != null){
            this.eventInvitationRepository.deleteEventInvitationById(invitationId);
            return ResponseEntity.ok(event);
        }
        this.eventInvitationRepository.deleteEventInvitationById(invitationId);
        return ResponseEntity.unprocessableEntity().body("Something went wrong");
    }

    @Transactional
    public ResponseEntity<?> declineInvite(Long invitationId){ //This should return boolean instead of responseEntity, was just for test
        this.eventInvitationRepository.deleteEventInvitationById(invitationId);
        HashMap<String, String> map = new HashMap<>();
        map.put("success", "Request deleted");
        return ResponseEntity.ok().body(map);
    }

    public boolean isInvitationValid(Long inviteId) {
        Long userId = this.userService.getCurrentUser().getId();
        int count = this.eventInvitationRepository.isInviteValid(userId, inviteId, new Date());
        return count > 0;
    }

}

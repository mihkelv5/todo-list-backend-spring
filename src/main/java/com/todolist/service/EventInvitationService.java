
package com.todolist.service;


import com.todolist.model.EventInvitationModel;
import com.todolist.model.EventModel;
import com.todolist.model.UserModel;
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

    public List<EventInvitationModel> findUserInvitations (UserModel user) {
        return this.eventInvitationRepository.findAllByInvitedUserAndExpirationDateIsAfterAndIsAccepted(user, new Date(), false);
    }




    public ResponseEntity<?> inviteUserToEvent (Long eventId, String username){
        EventModel event = this.eventRepository.findEventById(eventId);
        UserModel invitedUser = this.userService.findUserByUsername(username);
        UserModel requester = this.userService.getCurrentUser();
        if(this.eventInvitationRepository.existsByInvitedUserAndEventIdAndExpirationDateIsAfter(invitedUser, eventId, new Date())){
            return ResponseEntity.badRequest().body("UserModel " + username + "is already invited to the event!");
        }
        EventInvitationModel invitation = new EventInvitationModel();
        invitation.setRequesterUsername(requester.getUsername());
        invitation.setInvitedUser(invitedUser);
        invitation.setEventId(eventId);
        invitation.setEventName(event.getTitle());
        this.eventInvitationRepository.save(invitation);
        HashMap<String, String> map = new HashMap<>();
        map.put("success", "UserModel " + username + " invited to event!");
        return  ResponseEntity.ok().body(map);
    }

    @Transactional
    public ResponseEntity<?> acceptInvite(Long invitationId){ //This should return boolean instead of responseEntity, was just for test
        EventInvitationModel invitation = this.eventInvitationRepository.findEventInvitationByIdAndExpirationDateIsAfter(invitationId, new Date());

        EventModel event = this.eventService.saveUserToEvent(invitation.getEventId(), invitation.getInvitedUser().getUsername());

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

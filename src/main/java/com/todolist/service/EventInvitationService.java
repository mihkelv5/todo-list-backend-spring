
package com.todolist.service;


import com.todolist.entity.EventInvitationModel;
import com.todolist.entity.EventModel;
import com.todolist.entity.UserModel;
import com.todolist.repository.EventInvitationRepository;
import com.todolist.repository.EventRepository;
import org.springframework.stereotype.Service;


import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;


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

    //CREATE methods
    public boolean inviteUserToEvent (UUID eventId, String username){
        EventModel event = this.eventRepository.findEventById(eventId);
        UserModel invitedUser = this.userService.findUserByUsername(username);
        UserModel requester = this.userService.getCurrentUser();
        if(this.eventInvitationRepository.existsByInvitedUserAndEventIdAndExpirationDateIsAfter(invitedUser, eventId, new Date())){
            return false;
        }
        EventInvitationModel invitation = new EventInvitationModel();
        invitation.setRequesterUsername(requester.getUsername());
        invitation.setInvitedUser(invitedUser);
        invitation.setEventId(eventId);
        invitation.setEventName(event.getTitle());
        this.eventInvitationRepository.save(invitation);
        return true;
    }

    //READ methods
    public List<EventInvitationModel> findUserInvitations () {
        UserModel user = this.userService.getCurrentUser();
        return this.eventInvitationRepository.findAllByInvitedUserAndExpirationDateIsAfterAndIsAccepted(user, new Date(), false);
    }


    //READ methods for security
    public boolean isInvitationValid(UUID inviteId, UUID userId) {
        int count = this.eventInvitationRepository.isInviteValid(userId, inviteId, new Date());
        return count > 0;
    }


    //DELETE methods

    @Transactional
    public boolean acceptInvite(UUID invitationId){
        EventInvitationModel invitation = this.eventInvitationRepository.findEventInvitationByIdAndExpirationDateIsAfter(invitationId, new Date());

        EventModel event = this.eventService.saveUserToEvent(invitation.getEventId(), invitation.getInvitedUser().getUsername());

        //if everything goes correctly
        if(event != null){
            this.eventInvitationRepository.deleteEventInvitationById(invitationId);
            return true;
        }
        this.eventInvitationRepository.deleteEventInvitationById(invitationId); //if event is not found, still should delete the invite
        return false;
    }

    @Transactional
    public boolean declineInvite(UUID invitationId){
        this.eventInvitationRepository.deleteEventInvitationById(invitationId);
        return true;
    }


}

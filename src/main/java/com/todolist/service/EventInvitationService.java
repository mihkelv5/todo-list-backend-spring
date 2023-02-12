
package com.todolist.service;


import com.todolist.entity.EventInvitationModel;
import com.todolist.entity.EventModel;
import com.todolist.entity.UserModel;
import com.todolist.entity.dto.PublicUserDTO;
import com.todolist.repository.EventInvitationRepository;
import com.todolist.repository.EventRepository;
import org.springframework.stereotype.Service;


import jakarta.transaction.Transactional;

import java.util.*;


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

    public Set<PublicUserDTO> inviteUserToEvent (UUID eventId, Set<String> usernames){
        EventModel event = this.eventRepository.findEventById(eventId);
        UserModel requester = this.userService.getCurrentUser();
        Set<PublicUserDTO> invitedUsers = new HashSet<>();
        usernames.forEach(username -> {
            UserModel invitedUser = this.userService.findUserByUsername(username);
            if(this.eventInvitationRepository.existsByInvitedUserAndEventIdAndExpirationDateIsAfter(invitedUser, eventId, new Date())){
                return;
            }
            EventInvitationModel invitation = new EventInvitationModel();
            invitation.setRequesterUsername(requester.getUsername());
            invitation.setInvitedUser(invitedUser);
            invitation.setEvent(event);
            this.eventInvitationRepository.save(invitation);
            invitedUsers.add(PublicUserDTO.publicUserDTOConverter(invitedUser));
        });
        return invitedUsers;
    }

    //READ methods
    public List<EventInvitationModel> findUserInvitations () {
        UserModel user = this.userService.getCurrentUser();
        return this.eventInvitationRepository.findValidEventInvitesByUser(user, new Date(), false);
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

        EventModel event = this.eventService.saveUserToEvent(invitation.getEvent().getId(), invitation.getInvitedUser().getUsername());
        this.eventInvitationRepository.deleteEventInvitationById(invitationId); //if event is not found, invite is still deleted

        return event != null; //true if event exists.
    }

    @Transactional
    public boolean declineInvite(UUID invitationId){
        this.eventInvitationRepository.deleteEventInvitationById(invitationId);
        return true;
    }

    @Transactional
    public void deleteInvite(String username, UUID eventId){
        this.eventInvitationRepository.deleteEventInvitationModelByInvitedUserUsernameAndEventId(username, eventId);
    }


}

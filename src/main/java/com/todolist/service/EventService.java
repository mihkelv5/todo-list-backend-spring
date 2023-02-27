package com.todolist.service;

import com.todolist.entity.event.EventModel;
import com.todolist.entity.user.UserModel;
import com.todolist.entity.dto.EventModelDTO;
import com.todolist.entity.dto.PublicUserDTO;
import com.todolist.repository.EventInvitationRepository;
import com.todolist.repository.EventRepository;
import com.todolist.repository.TaskRepository;
import com.todolist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventInvitationRepository eventInvitationRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ProfilePictureService profilePictureService;

    @Autowired
    public EventService(EventRepository eventRepository, EventInvitationRepository eventInvitationRepository, TaskRepository taskRepository, UserService userService, UserRepository userRepository, ProfilePictureService profilePictureService) {
        this.eventRepository = eventRepository;
        this.eventInvitationRepository = eventInvitationRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.profilePictureService = profilePictureService;
    }

    //CREATE methods

    public EventModelDTO addEvent(EventModel event) {
        UserModel user = userService.getCurrentUser();
        event.registerUserToEvent(user);
        return EventModelDTO.EventModelDTOConverter(this.eventRepository.save(event));
    }

    //READ methods
    public EventModelDTO findEventById(UUID eventId){
        EventModel event = this.eventRepository.findEventById(eventId);
        EventModelDTO eventModelDTO = EventModelDTO.EventModelDTOConverter(event);
        Set<PublicUserDTO> invitedUsers = this.userService.getInvitedUsers(eventId);
        invitedUsers.forEach(user -> user.setImage(profilePictureService.downloadImageFromServer(user.getUsername()))); //kinda hacky way to add user images
        eventModelDTO.setInvitedUsers(invitedUsers);
        return eventModelDTO;
    }

    public List<EventModelDTO> findEventsByUser(){
        UserModel user = this.userService.getCurrentUser();
        List<EventModel> events = eventRepository.findEventsByUser(user);
        return events.stream().map(EventModelDTO::EventModelDTOConverter).collect(Collectors.toList());
    }

    //UPDATE methods
    public EventModelDTO updateEvent(EventModel updatedEvent){
        EventModel event = this.eventRepository.findEventById(updatedEvent.getId());
        event.setTitle(updatedEvent.getTitle());
        event.setDescription(updatedEvent.getDescription());
        eventRepository.save(event);
        return EventModelDTO.EventModelDTOConverter(event);
    }
    public EventModel saveUserToEvent(UUID eventId, String username){
        UserModel user = this.userRepository.findUserByUsername(username);
        EventModel event = this.eventRepository.findEventById(eventId);
        event.registerUserToEvent(user);
        return this.eventRepository.save(event);
    }



    //DELETE method
    @Transactional
    public void deleteEventById(UUID eventId) {
        this.taskRepository.deleteTasksByEventId(eventId);
        this.eventRepository.deleteEventById(eventId);
        this.eventInvitationRepository.deleteAllByEventId(eventId);
    }

    public boolean isUserInEvent(UUID eventId, UUID userId) {
        return this.eventRepository.isUserInEvent(eventId, userId);
    }


}

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

import java.util.ArrayList;
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
        return this.eventModelDTOConverter(this.eventRepository.save(event));
    }

    //READ methods
    public EventModelDTO findEventById(UUID eventId){
        EventModel event = this.eventRepository.findEventById(eventId);
        return this.eventModelDTOConverter(event);
    }

    public List<EventModelDTO> findEventsByUser(){
        UserModel user = this.userService.getCurrentUser();
        List<EventModel> events = eventRepository.findEventsByUser(user);
        return this.eventModelDTOListConverter(events);
    }

    //UPDATE methods
    public EventModelDTO updateEvent(EventModel updatedEvent){
        EventModel event = this.eventRepository.findEventById(updatedEvent.getId());
        event.setTitle(updatedEvent.getTitle());
        event.setDescription(updatedEvent.getDescription());
        eventRepository.save(event);
        return this.eventModelDTOConverter(event);
    }
    public EventModelDTO saveUserToEvent(UUID eventId, String username){
        UserModel user = this.userRepository.findUserByUsername(username);
        EventModel event = this.eventRepository.findEventById(eventId);
        event.registerUserToEvent(user);
        return this.eventModelDTOConverter(this.eventRepository.save(event));
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


    public EventModelDTO eventModelDTOConverter(EventModel eventModel){
        EventModelDTO eventModelDTO = new EventModelDTO();
        eventModelDTO.setId(eventModel.getId());
        eventModelDTO.setTitle(eventModel.getTitle());
        eventModelDTO.setDescription(eventModel.getDescription());

        Set<PublicUserDTO> eventUsers = this.userService.publicUserDTOSetConverter(eventModel.getEventUsers());
        eventModelDTO.setEventUsers(eventUsers);

        Set<PublicUserDTO> invitedUsers = this.userService.getInvitedUsers(eventModel.getId());
        eventModelDTO.setInvitedUsers(invitedUsers);

        return eventModelDTO;
    }

    public List<EventModelDTO> eventModelDTOListConverter(List<EventModel> eventModels){
        List<EventModelDTO> eventModelDTOList = new ArrayList<>();
        eventModels.forEach(eventModel -> eventModelDTOList.add(this.eventModelDTOConverter(eventModel)));
        return eventModelDTOList;
    }


}

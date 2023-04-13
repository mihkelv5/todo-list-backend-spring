package com.todolist.service;

import com.todolist.entity.event.EventModel;
import com.todolist.entity.user.UserModel;
import com.todolist.entity.dto.EventModelDTO;
import com.todolist.entity.dto.PublicUserDTO;
import com.todolist.repository.EventInvitationRepository;
import com.todolist.repository.EventRepository;
import com.todolist.repository.TaskRepository;
import com.todolist.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.*;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventInvitationRepository eventInvitationRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final UserService userService;


    @Autowired
    public EventService(EventRepository eventRepository, EventInvitationRepository eventInvitationRepository, TaskRepository taskRepository, UserService userService, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.eventInvitationRepository = eventInvitationRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.userService = userService;

    }

    //CREATE methods

    public EventModelDTO addEvent(EventModel event) {
        event.setEventUsers(new HashSet<>());
        event.setTaskTags(null);
        event.setInvites(new HashSet<>());
        UserModel user = userService.getCurrentUser();
        event.registerUserToEvent(user);
        return this.eventModelDTOConverter(this.eventRepository.save(event));
    }

    public String addEventTag(String newTag, UUID eventId) {
        EventModel eventModel = this.eventRepository.findEventById(eventId);
        if(eventModel.getTaskTags() == null){
            eventModel.setTaskTags(newTag);
        } else {
            eventModel.setTaskTags(eventModel.getTaskTags() + ", " + newTag);
        }
        this.eventRepository.save(eventModel);
        return newTag;
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

    @Transactional
    public void deleteEventTag(String tag, UUID eventId) {
        EventModel eventModel = this.eventRepository.findEventById(eventId);
        List<String> tags = new ArrayList<>(Arrays.asList(eventModel.getTaskTags().split(", ")));
        tags.remove(tag);
        if(tags.size() > 0){
            String newTags = StringUtils.join(tags, ", ");
            eventModel.setTaskTags(newTags);
        } else {
            eventModel.setTaskTags(null);
        }
        this.eventRepository.save(eventModel);
    }

    public boolean isUserInEvent(UUID eventId, UUID userId) {
        return this.eventRepository.isUserInEvent(eventId, userId);
    }


    public EventModelDTO eventModelDTOConverter(EventModel eventModel){
        EventModelDTO eventModelDTO = new EventModelDTO();
        eventModelDTO.setId(eventModel.getId());
        eventModelDTO.setTitle(eventModel.getTitle());
        eventModelDTO.setDescription(eventModel.getDescription());
        eventModelDTO.setTaskTags(eventModel.getTaskTags());

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

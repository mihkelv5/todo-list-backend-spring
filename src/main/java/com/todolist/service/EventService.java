package com.todolist.service;

import com.todolist.model.EventModel;
import com.todolist.model.UserModel;
import com.todolist.repository.EventInvitationRepository;
import com.todolist.repository.EventRepository;
import com.todolist.repository.TaskRepository;
import com.todolist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.HashSet;
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

    @Autowired
    public EventService(EventRepository eventRepository, EventInvitationRepository eventInvitationRepository, TaskRepository taskRepository, UserService userService, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.eventInvitationRepository = eventInvitationRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    //CREATE methods

    public EventModel addEvent(EventModel event) {
        UserModel user = userService.getCurrentUser();
        event.registerUserToEvent(user);
        return this.eventRepository.save(event);
    }

    //READ methods
    public EventModel findEventById(UUID eventId){
        EventModel event = this.eventRepository.findEventById(eventId);
        event.setEventUsernames(event.getEventUsers().stream().map(UserModel::getUsername).collect(Collectors.toSet()));
        return event;
    }

    public List<EventModel> findEventsByUser(){
        UserModel user = this.userService.getCurrentUser();
        Set<UserModel> users = new HashSet<>();
        users.add(user);
        return eventRepository.findEventsByEventUsersIn(users);
    }

    //UPDATE methods
    public EventModel updateEvent(EventModel updatedEvent){
        EventModel event = this.eventRepository.findEventById(updatedEvent.getId());
        event.setTitle(updatedEvent.getTitle());
        event.setDescription(updatedEvent.getDescription());
        eventRepository.save(event);
        return event;
    }
    public EventModel saveUserToEvent(UUID eventId, String username){
        UserModel user = this.userRepository.findByUsername(username);
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
        return this.eventRepository.existsEventModelByIdAndEventUsersId(eventId, userId);
    }
}

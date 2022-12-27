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

    public EventModel findEventById(UUID eventId){
        EventModel event = this.eventRepository.findEventById(eventId);
        event.setEventUsernames(event.getEventUsers().stream().map(UserModel::getUsername).collect(Collectors.toSet()));
        return event;
    }

    public List<EventModel> findEventsByUser(UserModel user){
        Set<UserModel> users = new HashSet<>();
        users.add(user);
        return eventRepository.findEventsByEventUsersIn(users);
    }

    public EventModel addEvent(EventModel event) {
        UserModel user = userService.getCurrentUser();
        event.registerUserToEvent(user);
        return this.eventRepository.save(event);
    }

    public EventModel updateEvent(EventModel event){
        EventModel oldEvent = this.eventRepository.findEventById(event.getId());
        oldEvent.setTitle(event.getTitle());
        oldEvent.setDescription(event.getDescription());
        eventRepository.save(oldEvent);
        return oldEvent;
    }

    public EventModel saveUserToEvent(UUID eventId, String username){
        UserModel user = this.userRepository.findByUsername(username);
        EventModel event = this.eventRepository.findEventById(eventId);
        event.registerUserToEvent(user);
        return this.eventRepository.save(event);
    }

    public List<UserModel> findUsersByEvent(UUID eventId) {
        EventModel event = this.eventRepository.findEventById(eventId);
        return userService.findUsersByEvent(event);
    }

    @Transactional
    public void deleteEventById(UUID eventId) {
        this.taskRepository.deleteTasksByEventId(eventId);
        this.eventRepository.deleteEventById(eventId);
        this.eventInvitationRepository.deleteAllByEventId(eventId);
    }

}

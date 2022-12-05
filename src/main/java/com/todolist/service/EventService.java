package com.todolist.service;

import com.todolist.model.Event;
import com.todolist.model.User;
import com.todolist.repository.EventRepository;
import com.todolist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserService userService;

    @Autowired
    public EventService(EventRepository eventRepository, UserService userService) {
        this.eventRepository = eventRepository;
        this.userService = userService;
    }

    public Event findEventById(Long id){
        return this.eventRepository.findEventById(id);
    }

    public List<Event> findEventsByUser(Long userId){
        User user = this.userService.findUserById(userId);
        Set<User> users = new HashSet<>();
        users.add(user);
        return eventRepository.findEventsByEventUsersIn(users);
    }

    public Event addEvent(Event event) {
        User user = userService.getCurrentUser();
        event.registerUserToEvent(user);
        return this.eventRepository.save(event);
    }

    public Event saveUserToEvent(Long eventId, Long userId){
        User user = this.userService.findUserById(userId);
        Event event = this.eventRepository.findEventById(eventId);
        event.registerUserToEvent(user);
        return this.eventRepository.save(event);
    }

    public List<User> findUsersByEvent(Long eventId) {
        Event event = this.eventRepository.findEventById(eventId);
        return userService.findUsersByEvent(event);
    }


}

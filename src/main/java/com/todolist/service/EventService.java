package com.todolist.service;

import com.todolist.model.Event;
import com.todolist.model.User;
import com.todolist.repository.EventRepository;
import com.todolist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public EventService(EventRepository eventRepository, UserService userService, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public Event findEventById(Long id){
        return this.eventRepository.findEventById(id);
    }

    public List<Event> findEventsByUser(String username){
        User user = this.userService.findUserByUsername(username);
        Set<User> users = new HashSet<>();
        users.add(user);
        return eventRepository.findEventsByEventUsersIn(users);
    }

    public Event addEvent(Event event) {
        User user = userService.getCurrentUser();
        event.registerUserToEvent(user);
        return this.eventRepository.save(event);
    }

    public Event updateEvent(Event event){
        Event oldEvent = this.eventRepository.findEventById(event.getId());
        oldEvent.setTitle(event.getTitle());
        oldEvent.setDescription(event.getDescription());
        eventRepository.save(oldEvent);
        return oldEvent;
    }

    public Event saveUserToEvent(Long eventId, String username){
        User user = this.userRepository.findByUsername(username);
        Event event = this.eventRepository.findEventById(eventId);
        event.registerUserToEvent(user);
        return this.eventRepository.save(event);
    }

    public List<User> findUsersByEvent(Long eventId) {
        Event event = this.eventRepository.findEventById(eventId);
        return userService.findUsersByEvent(event);
    }

    @Transactional
    public void deleteEventById(Long eventId) {
        this.eventRepository.deleteEventById(eventId);
    }

}

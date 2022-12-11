package com.todolist.controller;

import com.todolist.model.Event;
import com.todolist.model.User;
import com.todolist.service.EventService;
import com.todolist.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping(path = "/api/event")
public class EventController {

    private final EventService eventService;
    private final UserService userService;


    public EventController(EventService eventService, UserService userService) {
        this.eventService = eventService;

        this.userService = userService;
    }

    @GetMapping("/find/{eventId}")
    public ResponseEntity<Event> findEventById(@PathVariable("eventId") Long eventId){
        Event event = this.eventService.findEventById(eventId);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/user/all") //can be done with current user
    public ResponseEntity<List<Event>> findEventsByUserId(){
        User user = userService.getCurrentUser();
        List<Event> events = this.eventService.findEventsByUser(user);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{eventId}/users")
    public ResponseEntity<List<User>> findUsersByEventId(@PathVariable("eventId") Long eventId){
        List<User> users = this.eventService.findUsersByEvent(eventId);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/add")
    public ResponseEntity<Event> addEvent(@RequestBody Event event){
        //eventService automatically registers the jwt holder as a user
        Event addedEvent = this.eventService.addEvent(event);
        return ResponseEntity.ok(addedEvent);
    }

    @PutMapping("/update")
    public ResponseEntity<Event> updateEvent(@RequestBody Event event) {
        Event updatedEvent = this.eventService.updateEvent(event);
        return ResponseEntity.ok(updatedEvent);
    }

    @Transactional
    @DeleteMapping("delete/{eventId}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long eventId){
        this.eventService.deleteEventById(eventId);
        return ResponseEntity.ok().build();
    }


    //for development
    @PutMapping("{eventId}/register/{username}")
    public ResponseEntity<?> registerUserToEvent(@PathVariable("eventId") Long eventId, @PathVariable("username") String username) {
        Event event = this.eventService.saveUserToEvent(eventId, username);
        return ResponseEntity.ok(event);
    }


}

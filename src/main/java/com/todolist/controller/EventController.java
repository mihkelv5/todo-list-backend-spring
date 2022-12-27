package com.todolist.controller;

import com.todolist.model.EventModel;
import com.todolist.model.UserModel;
import com.todolist.service.EventService;
import com.todolist.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;

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
    @PreAuthorize("@preAuthFilter.checkIfUserInEvent(#eventId)")
    public ResponseEntity<EventModel> findEventById(@PathVariable("eventId") UUID eventId){
        EventModel event = this.eventService.findEventById(eventId);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/user/all") //can be done with current user
    public ResponseEntity<List<EventModel>> findEventsByUserId(){
        UserModel user = userService.getCurrentUser();
        List<EventModel> events = this.eventService.findEventsByUser(user);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{eventId}/users")
    @PreAuthorize("@preAuthFilter.checkIfUserInEvent(#eventId)")
    public ResponseEntity<List<UserModel>> findUsersByEventId(@PathVariable("eventId") UUID eventId){
        List<UserModel> users = this.eventService.findUsersByEvent(eventId);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/add")
    public ResponseEntity<EventModel> addEvent(@RequestBody EventModel event){
        //eventService automatically registers the jwt holder as a user
        EventModel addedEvent = this.eventService.addEvent(event);
        return ResponseEntity.ok(addedEvent);
    }

    @PutMapping("/update")
    @PreAuthorize("@preAuthFilter.checkIfUserInEvent(#event.id)") //TODO: Admin system for events
    public ResponseEntity<EventModel> updateEvent(@RequestBody EventModel event) {
        EventModel updatedEvent = this.eventService.updateEvent(event);
        return ResponseEntity.ok(updatedEvent);
    }

    @Transactional
    @DeleteMapping("delete/{eventId}")
    @PreAuthorize("@preAuthFilter.checkIfUserInEvent(#eventId)") //TODO: Admin system for events
    public ResponseEntity<?> deleteEvent(@PathVariable UUID eventId){
        this.eventService.deleteEventById(eventId);
        return ResponseEntity.ok().build();
    }



    @PutMapping("{eventId}/register/{username}") //for development, will be removed in production
    public ResponseEntity<?> registerUserToEvent(@PathVariable("eventId") UUID eventId, @PathVariable("username") String username) {
        EventModel event = this.eventService.saveUserToEvent(eventId, username);
        return ResponseEntity.ok(event);
    }


}

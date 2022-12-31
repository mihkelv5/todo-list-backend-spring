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

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/find/{eventId}")
    @PreAuthorize("@preAuthFilter.checkIfUserInEvent(#eventId)")
    public ResponseEntity<EventModel> findEventById(@PathVariable("eventId") UUID eventId){
        EventModel event = this.eventService.findEventById(eventId);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/user/all") //can be done with current user
    public ResponseEntity<List<EventModel>> findEventsByUserId(){
        List<EventModel> events = this.eventService.findEventsByUser();
        return ResponseEntity.ok(events);
    }



    @PostMapping("/add")
    public ResponseEntity<EventModel> addEvent(@RequestBody EventModel event){
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



}

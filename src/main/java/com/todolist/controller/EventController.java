package com.todolist.controller;

import com.todolist.entity.EventModel;
import com.todolist.entity.dto.EventModelDTO;
import com.todolist.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/event")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/find/{eventId}")
    @PreAuthorize("@preAuthMethodFilter.checkIfUserInEvent(#eventId)")
    public ResponseEntity<EventModelDTO> findEventById(@PathVariable("eventId") UUID eventId){
        EventModelDTO event = this.eventService.findEventById(eventId);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/all") //can be done with current user
    public ResponseEntity<Set<EventModelDTO>> findEventsByUserId(){
        Set<EventModelDTO> events = this.eventService.findEventsByUser();
        return ResponseEntity.ok(events);
    }

    @PostMapping("/add")
    public ResponseEntity<EventModelDTO> addEvent(@RequestBody EventModel event){
        EventModelDTO addedEvent = this.eventService.addEvent(event);
        return ResponseEntity.ok(addedEvent);
    }

    @PutMapping("/update")
    @PreAuthorize("@preAuthMethodFilter.checkIfUserInEvent(#event.id)") //TODO: Admin system for events
    public ResponseEntity<EventModelDTO> updateEvent(@RequestBody EventModel event) {
        EventModelDTO updatedEvent = this.eventService.updateEvent(event);
        return ResponseEntity.ok(updatedEvent);
    }

    @Transactional
    @DeleteMapping("/delete/{eventId}")
    @PreAuthorize("@preAuthMethodFilter.checkIfUserInEvent(#eventId)") //TODO: Admin system for events
    public ResponseEntity<?> deleteEvent(@PathVariable UUID eventId){
        this.eventService.deleteEventById(eventId);
        return ResponseEntity.ok().build();
    }

}

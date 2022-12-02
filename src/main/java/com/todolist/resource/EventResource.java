package com.todolist.resource;

import com.todolist.model.Event;
import com.todolist.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/event")
public class EventResource {

    private final EventService eventService;

    public EventResource(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Event> findEventById(@PathVariable("id") Long id){
        Event event = this.eventService.findEventById(id);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Event>> findEventsByUserId(@PathVariable("userId") Long id){
        List<Event> events = this.eventService.findEventsByUser(id);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Event> addEvent(@RequestBody Event event){
        //eventService automatically registers the jwt holder as a user
        Event addedEvent = this.eventService.addEvent(event);
        return ResponseEntity.ok(addedEvent);
    }

    @PutMapping("{eventId}/register/{userId}")
    public ResponseEntity<?> registerUserToEvent(@PathVariable("eventId") Long eventId, @PathVariable("userId") Long userId) {
        Event event = this.eventService.saveUserToEvent(eventId, userId);
        return ResponseEntity.ok(event);
    }


}

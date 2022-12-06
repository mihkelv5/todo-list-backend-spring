package com.todolist.resource;

import com.todolist.model.Event;
import com.todolist.model.EventInvitation;
import com.todolist.model.User;
import com.todolist.service.EventInvitationService;
import com.todolist.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/event")
public class EventResource {

    private final EventService eventService;
    private final EventInvitationService eventInvitationService;

    public EventResource(EventService eventService, EventInvitationService eventInvitationService) {
        this.eventService = eventService;
        this.eventInvitationService = eventInvitationService;
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

    @PutMapping("{eventId}/register/{userId}")
    public ResponseEntity<?> registerUserToEvent(@PathVariable("eventId") Long eventId, @PathVariable("userId") Long userId) {
        Event event = this.eventService.saveUserToEvent(eventId, userId);
        return ResponseEntity.ok(event);
    }

    @PutMapping("/{eventId}/invite/{userId}")
    public ResponseEntity<?> inviteUserToEvent(@PathVariable("eventId") Long eventId, @PathVariable("userId") Long userId) {
        boolean inviteSuccess = this.eventInvitationService.inviteUserToEvent(eventId, userId);
        if(inviteSuccess) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.unprocessableEntity().build();
    }

    @GetMapping("/invites/{userId}")
    public ResponseEntity<List<EventInvitation>> getUserEventInvitations(@PathVariable("userId") Long userId){
        List<EventInvitation> eventInvitationList = this.eventInvitationService.findUserInvitations(userId);
        return ResponseEntity.ok(eventInvitationList);
    }

    @PutMapping("/accept/{invitationId}")
    public ResponseEntity<?> acceptEventInvitation(@PathVariable ("invitationId") Long invitationId) {
        return this.eventInvitationService.acceptInvite(invitationId);
    }
}

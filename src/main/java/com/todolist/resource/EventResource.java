package com.todolist.resource;

import com.todolist.model.Event;
import com.todolist.model.EventInvitation;
import com.todolist.model.User;
import com.todolist.service.EventInvitationService;
import com.todolist.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
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

    @GetMapping("/find/{eventId}")
    public ResponseEntity<Event> findEventById(@PathVariable("eventId") Long eventId){
        Event event = this.eventService.findEventById(eventId);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/user/{userId}") //can be done with current user
    public ResponseEntity<List<Event>> findEventsByUserId(@PathVariable("userId") Long id){
        List<Event> events = this.eventService.findEventsByUser(id);
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

    @PutMapping("{eventId}/register/{username}")
    public ResponseEntity<?> registerUserToEvent(@PathVariable("eventId") Long eventId, @PathVariable("username") String username) {
        Event event = this.eventService.saveUserToEvent(eventId, username);
        return ResponseEntity.ok(event);
    }



    @PutMapping("/invite/{eventId}/user/{username}")
    public ResponseEntity<?> inviteUserToEvent(@PathVariable("eventId") Long eventId, @PathVariable("username") String username) {
        return this.eventInvitationService.inviteUserToEvent(eventId, username);
    }

    @GetMapping("/invite/{username}/get/all") //TODO: make filter so only invited user can see the invitations
    public ResponseEntity<List<EventInvitation>> getUserEventInvitations(@PathVariable("username") String username){
        List<EventInvitation> eventInvitationList = this.eventInvitationService.findUserInvitations(username);
        return ResponseEntity.ok(eventInvitationList);
    }

    @PutMapping("/invite/accept/{invitationId}") //TODO: filter so only invited user can accept the invitation
    public ResponseEntity<?> acceptEventInvitation(@PathVariable ("invitationId") Long invitationId) {
        return this.eventInvitationService.acceptInvite(invitationId);
    }
    @Transactional
    @DeleteMapping("/invite/decline/{invitationId}")
    public ResponseEntity<?> declineEventInvitation(@PathVariable("invitationId") Long invitationId){
        return this.eventInvitationService.declineInvite(invitationId);
    }
}

package com.todolist.repository;

import com.todolist.entity.event.EventModel;
import com.todolist.entity.user.UserModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class EventRepositoryTest {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserRepository userRepository;

    @AfterEach
    void tearDown() {
        this.eventRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    @Test
    void shouldSaveEvent(){
        //setup
        EventModel event = new EventModel("title", "description");

        //query
        EventModel actualEvent = this.eventRepository.save(event);

        //assert
        assertNotNull(actualEvent);

    }

    @Test
    void shouldFindEventById() {
        EventModel event = new EventModel("title", "description");
        UUID eventId = this.eventRepository.save(event).getId();

        //query
        EventModel actualEvent = this.eventRepository.findEventById(eventId);

        //assert
        assertNotNull(actualEvent);
        assertEquals("title", actualEvent.getTitle());
    }

    @Test
    void shouldFindEventsByEventUsers() {
        //setup
        UserModel user = new UserModel();
        user.setUsername("username");
        user.setEmail("email");
        UserModel actualUser = this.userRepository.save(user);

        EventModel event = new EventModel("title", "description");
        event.registerUserToEvent(user);
        EventModel actualEvent = this.eventRepository.save(event);

        //query
        List<EventModel> events = this.eventRepository.findEventsByUser(actualUser);

        //assert
        assertTrue(events.contains(actualEvent));

    }

    @Test
    void shouldDeleteEventById() {
        //setup

        for (int i = 0; i < 10; i++) {
            EventModel event = new EventModel("title" + i, "description");
            this.eventRepository.save(event);
        }
            //selects a random event and deletes is
        UUID eventId = this.eventRepository.findAll().get(ThreadLocalRandom.current().nextInt(0, 9)).getId();


        //query
        this.eventRepository.deleteEventById(eventId);
        List<EventModel> remainingEvents = this.eventRepository.findAll();

        //assert
        remainingEvents.forEach(event -> assertNotEquals(eventId, event.getId()));

    }

    @Test
    void shouldCheckIfUserInEvent() {
        //setup
        EventModel event = new EventModel("title", "description");

        UserModel userInEvent = new UserModel();
        userInEvent.setUsername("username");
        userInEvent.setEmail("email");

        UserModel userNotInEvent = new UserModel();
        userNotInEvent.setUsername("username2");
        userNotInEvent.setEmail("email2");

        UUID userNotInEventId = this.userRepository.save(userNotInEvent).getId();
        UUID userInEventId = this.userRepository.save(userInEvent).getId();
        event.registerUserToEvent(userInEvent);

        UUID eventId = this.eventRepository.save(event).getId();

        //query
        boolean userShouldBeInEvent = this.eventRepository.isUserInEvent(eventId, userInEventId);
        boolean userShouldNotBeInEvent = this.eventRepository.isUserInEvent(eventId, userNotInEventId);

        //assert
        assertTrue(userShouldBeInEvent);
        assertFalse(userShouldNotBeInEvent);

    }
}
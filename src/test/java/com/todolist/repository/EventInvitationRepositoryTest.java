package com.todolist.repository;

import com.todolist.entity.EventInvitationModel;
import com.todolist.entity.EventModel;
import com.todolist.entity.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EventInvitationRepositoryTest {

    @Autowired
    private EventInvitationRepository eventInvitationTestRepository;
    @Autowired
    private UserRepository userTestRepository;
    @Autowired
    private EventRepository eventTestRepository;

    @BeforeEach
    void setUp() {

    }

    @Test
    void deleteEventInvitationById() {
        //setup
        UserModel user = new UserModel();
        user.setUsername("user");
        user.setEmail("email");
        this.userTestRepository.save(user);

        EventModel event = new EventModel();
        event.setTitle("event");
        this.eventTestRepository.save(event);

        EventInvitationModel invite = new EventInvitationModel();
        invite.setEvent(event);
        invite.setInvitedUser(user);
        invite.setRequesterUsername("test");
        UUID inviteId =  this.eventInvitationTestRepository.save(invite).getId();

        //query
        boolean inviteExistsBeforeDelete = this.eventInvitationTestRepository.findById(inviteId).isPresent();

        this.eventInvitationTestRepository.deleteEventInvitationById(inviteId);

        boolean inviteExistsAfterDelete = this.eventInvitationTestRepository.findById(inviteId).isPresent();

        //assert
        assertTrue(inviteExistsBeforeDelete);
        assertFalse(inviteExistsAfterDelete);
    }

    @Test
    void deleteEventInvitationModelByInvitedUserUsernameAndEventId() {
        //setup
        UserModel user = new UserModel();
        user.setUsername("user");
        user.setEmail("email");
        this.userTestRepository.save(user);

        EventModel event = new EventModel();
        event.setTitle("event");
        UUID eventId = this.eventTestRepository.save(event).getId();

        EventInvitationModel invite = new EventInvitationModel();
        invite.setEvent(event);
        invite.setInvitedUser(user);
        invite.setRequesterUsername("test");
        UUID inviteId = this.eventInvitationTestRepository.save(invite).getId();


        //query
        boolean inviteExistsBeforeDelete = this.eventInvitationTestRepository.findById(inviteId).isPresent();

        this.eventInvitationTestRepository.deleteEventInvitationModelByInvitedUserUsernameAndEventId(user.getUsername(), eventId);

        boolean inviteExistsAfterDelete = this.eventInvitationTestRepository.findById(inviteId).isPresent();

        //assert
        assertTrue(inviteExistsBeforeDelete);
        assertFalse(inviteExistsAfterDelete);

    }

    @Test
    void findValidEventInvitesByUser() {

    }

    @Test
    void existsByInvitedUserAndEventIdAndExpirationDateIsAfter() {
    }

    @Test
    void findEventInvitationByIdAndExpirationDateIsAfter() {
    }

    @Test
    void deleteAllByEventId() {
    }

    @Test
    void isInviteValid() {
    }
}
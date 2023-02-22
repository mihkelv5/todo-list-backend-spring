package com.todolist.repository;

import com.todolist.entity.event.EventInvitationModel;
import com.todolist.entity.event.EventModel;
import com.todolist.entity.user.UserModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private EventInvitationRepository eventInvitationTestRepository;
    @Autowired
    private UserRepository userTestRepository;
    @Autowired
    private EventRepository eventTestRepository;

    @BeforeEach
    void setUp() {
        UserModel user = new UserModel();
        user.setUsername("username");
        user.setEmail("email");
        this.userTestRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        userTestRepository.deleteAll();
    }

    @Test
    void doesExistByEmail() {
        //additional setup not needed

        //query
        boolean existsByEmail1 = this.userTestRepository.existsByUserEmail("EMAIL");
        boolean existsByEmail2 = this.userTestRepository.existsByUserEmail("email");
        boolean doesNotExistByEmail = this.userTestRepository.existsByUserEmail("shouldNotExist");

        //assert
        assertTrue(existsByEmail1);
        assertTrue(existsByEmail2);
        assertFalse(doesNotExistByEmail);

    }

    @Test
    void existsByUsername() {
        //no additional setup needed

        //query
        boolean existsByUsername1 = this.userTestRepository.existsUserByUsername("username");
        boolean existsByUsername2 = this.userTestRepository.existsUserByUsername("USERNAME");
        boolean doesNotCheckMailAddress = this.userTestRepository.existsUserByUsername("email");
        boolean doesNotExistByUsername = this.userTestRepository.existsUserByUsername("shouldNotExist");

        //assert
        assertTrue(existsByUsername1);
        assertTrue(existsByUsername2);
        assertFalse(doesNotCheckMailAddress);
        assertFalse(doesNotExistByUsername);

    }

    @Test
    void findByUsername() {
        //no additional setup needed

        //query
        UserModel user = this.userTestRepository.findUserByUsername("username");
        UserModel sameUser = this.userTestRepository.findUserByUsername("USERNAME"); //tests if not case-sensitive
        UserModel noUser = this.userTestRepository.findUserByUsername("shouldNotExist");

        //assert
        assertNotNull(user);
        assertNotNull(sameUser);
        assertNull(noUser);
        assertEquals(user.getUsername(), sameUser.getUsername());
    }

    @Test
    void findUserByEmail() {
        //no additional setup needed

        //query
        UserModel user = this.userTestRepository.findUserByEmail("email");
        UserModel sameUser = this.userTestRepository.findUserByEmail("EMAIL");
        UserModel noUser = this.userTestRepository.findUserByEmail("shouldNotExist");

        //assert
        assertNotNull(user);
        assertNotNull(sameUser);
        assertNull(noUser);
        assertEquals(user.getUsername(), sameUser.getUsername());
    }

    @Test
    void findUserById() {
        //setup
        UUID userId = this.userTestRepository.findAll().get(0).getId();

        //query
        UserModel user = this.userTestRepository.findUserById(userId);

        //assert
        assertNotNull(user);
        assertEquals(user.getId(), userId);
    }

    @Test
    void findUsersByEventsId() {
        //setup
        EventModel event = new EventModel("title", "description");
        event = this.eventTestRepository.save(event);

        Set<UserModel> users = new HashSet<>();
        //create 10 users, register half of them to event
        for (int i = 0; i <10 ; i++) {
            UserModel tempUser = new UserModel();
            tempUser.setUsername("user" + i);
            tempUser.setEmail("email" + i);
            if(i % 2 == 0){
                event.registerUserToEvent(tempUser);
            }
            users.add(tempUser);
        }
        this.userTestRepository.saveAll(users);
        this.eventTestRepository.save(event);

        //query
        Set<UserModel> eventUsers = this.userTestRepository.findUsersByEventsId(event.getId());

        //assert
        assertEquals(event.getEventUsers().size(), eventUsers.size());
        assertNotEquals(users.size(), eventUsers.size());
    }

    @Test
    void existsUserModelByUsernameAndEventsId() {
        //setup
        EventModel event = new EventModel("title", "description");
        UserModel user = this.userTestRepository.findUserByUsername("username");
        event.registerUserToEvent(user);
        UUID eventID = this.eventTestRepository.save(event).getId();

        //query
        boolean shouldExistUser = this.userTestRepository.existsUserModelByUsernameAndEventsId(user.getUsername(), eventID);
        boolean shouldExistUserUppercase = this.userTestRepository.existsUserModelByUsernameAndEventsId(user.getUsername().toUpperCase(), eventID);
        boolean shouldNotExistUser = this.userTestRepository.existsUserModelByUsernameAndEventsId("shouldNotExist", eventID);
        boolean shouldNotExistEvent = this.userTestRepository.existsUserModelByUsernameAndEventsId(user.getUsername(), UUID.randomUUID());

        //assert
        assertTrue(shouldExistUser);
        assertTrue(shouldExistUserUppercase);
        assertFalse(shouldNotExistUser);
        assertFalse(shouldNotExistEvent);
    }

    @Test
    void existsUserModelByIdAndEventsId() {
        EventModel event = new EventModel("title", "description");
        UserModel user = this.userTestRepository.findUserByUsername("username");
        event.registerUserToEvent(user);
        UUID eventID = this.eventTestRepository.save(event).getId();

        //query
        boolean shouldExistUser = this.userTestRepository.existsUserModelByIdAndEventsId(user.getId(), eventID);
        boolean shouldNotExistUser = this.userTestRepository.existsUserModelByIdAndEventsId(UUID.randomUUID(), eventID);
        boolean shouldNotExistEvent = this.userTestRepository.existsUserModelByIdAndEventsId(user.getId(), UUID.randomUUID());

        //assert
        assertTrue(shouldExistUser);
        assertFalse(shouldNotExistUser);
        assertFalse(shouldNotExistEvent);
    }

    @Test
    void findUsersNotInEvent() {
        //setup
        EventModel event = new EventModel("title", "description");
        event = this.eventTestRepository.save(event);

        Set<UserModel> users = new HashSet<>();
            //create 10 users, register half of them to event
        for (int i = 0; i <10 ; i++) {
            UserModel tempUser = new UserModel();
            tempUser.setUsername("user" + i);
            tempUser.setEmail("email" + i);
            if(i % 2 == 0){
                event.registerUserToEvent(tempUser);
            }
            users.add(tempUser);
        }
        this.userTestRepository.saveAll(users);
        this.eventTestRepository.save(event);

        //query
        Set<UserModel> notInEventUsers = this.userTestRepository.findUsersNotInEventByEventId(event.getId());

        //assert
        assertNotNull(notInEventUsers);
        notInEventUsers.forEach(user -> assertEquals(0, user.getEvents().size()));
    }

    @Test
    void findAllUsersByUsernameSet() {
        //setup
        Set<String> usernames = new HashSet<>();

        for (int i = 0; i < 10; i++) {
            UserModel user = new UserModel();
            user.setUsername("user" + i);
            user.setEmail("email" + i);
            usernames.add(user.getUsername());
            this.userTestRepository.save(user);
        }


        //query
        Set<UserModel> actualUsers = this.userTestRepository.findAllUsersByUsernameSet(usernames);

        //assert
        assertEquals(usernames.size(), actualUsers.size());
        actualUsers.forEach(actualUser -> assertTrue(usernames.contains(actualUser.getUsername())));
    }

    @Test
    void findAlreadyInvitedUsersByEventId() throws ParseException {
        EventModel event = new EventModel("title", "description");
        event = this.eventTestRepository.save(event);


        for (int i = 0; i < 10; i++) {
            UserModel user = new UserModel();
            user.setUsername("user" + i);
            user.setEmail("email" + i);
            this.userTestRepository.save(user);
            if(i % 2 == 0){
                EventInvitationModel invite = generateEventInvitationModel(user);
                invite.setEvent(event);
                eventInvitationTestRepository.save(invite);
            }
        }

        //query
        Set<UserModel> alreadyInvitedUsers = this.userTestRepository.findAlreadyInvitedUsersByEventId(event.getId());

        //assert
        assertEquals(5, alreadyInvitedUsers.size());
    }

    private EventInvitationModel generateEventInvitationModel(UserModel user) throws ParseException {
        Date date = new SimpleDateFormat("dd/MM/yyyy").parse("10/10/3000");
        return new EventInvitationModel("randomUser",  false, false, date, user);
    }
}
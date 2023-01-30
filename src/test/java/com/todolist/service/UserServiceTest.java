package com.todolist.service;

import com.todolist.entity.UserModel;
import com.todolist.entity.dto.UserCreationDTO;
import com.todolist.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.security.auth.login.CredentialNotFoundException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void shouldAddUser() throws CredentialNotFoundException {

        //setup
        UserModel user = new UserModel();
        user.setId(UUID.randomUUID());
        user.setUsername("test");
        user.setEmail("email");

        when(userRepository.save(any(UserModel.class))).thenReturn(user);

        UserCreationDTO userDTO = new UserCreationDTO("test", "password", "email");

        //run
        UserModel actual = userService.addUser(userDTO);

        //assert
        assert(user.getUsername().equals(actual.getUsername()));
        assert(user.getEmail().equals(actual.getEmail()));

    }

    @Test
    void shouldNotAddUser(){
        //setup
        UserCreationDTO user = new UserCreationDTO();

        //run
        Exception exception = assertThrows(CredentialNotFoundException.class, () -> {
            userService.addUser(user);
        });

        String errorMessage = exception.getMessage();
        String expectedMessage = "User must have username, email and password";

        //assert
        assertEquals(errorMessage, expectedMessage);

    }

    @Test
    void findUserByUsername() {
        //setup
        UserModel user = new UserModel();
        user.setId(UUID.randomUUID());
        user.setUsername("test");
        user.setEmail("email");
        when(userRepository.findUserByUsername(anyString())).thenReturn(user);

        //run
        UserModel actual = this.userService.findUserByUsername(MethodOrderer.Random.RANDOM_SEED_PROPERTY_NAME);

        //assert
        assert(user.getUsername().equals(actual.getUsername()));
        assert(user.getEmail().equals(actual.getEmail()));
    }

    @Test
    void shouldNotFindUserByUsernameThatDoesNotExist() {

        //setup
        when(userRepository.findUserByUsername(anyString())).thenReturn(null);

        //run
        UserModel actual = this.userService.findUserByUsername(MethodOrderer.Random.RANDOM_SEED_PROPERTY_NAME);

        //assert
        assert(actual == null);
    }

    @Test
    void findAllUsersByUsernames() { //This test seems very iffy...
        //setup
        Set<UserModel> userModelSet = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            UserModel user = new UserModel();
            user.setUsername("user" + i);
            userModelSet.add(user);
        }

        Set<String> usernames1 = Set.of("user1", "user2");
        when(userRepository.findAllUsersByUsernameSet(usernames1))
                .thenReturn(userModelSet.stream()
                        .filter(userModel -> usernames1.contains(userModel.getUsername()))
                        .collect(Collectors.toSet()));

        Set<String> usernames2 = Set.of("user2", "user3", "user4");
        when(userRepository.findAllUsersByUsernameSet(usernames2))
                .thenReturn(userModelSet.stream()
                        .filter(userModel -> usernames2.contains(userModel.getUsername()))
                        .collect(Collectors.toSet()));

        //run
        Set<UserModel> actual1 = this.userService.findAllUsersByUsernames(Set.of("user1", "user2"));
        Set<UserModel> actual2 = this.userService.findAllUsersByUsernames(Set.of("user2", "user3", "user4"));

        //assert
        assert(actual1.size() == 2);
        assert(actual2.size() == 3);
    }

    @Test
    void findUsersNotInEvent() {

    }

    @Test
    void findUsersByEventId() {
    }

    @Test
    void isUsernameInEvent() {
    }

    @Test
    void isUserIdInEvent() {
    }

    @Test
    void getInvitedUsers() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void shouldActivateUser() {
        //setup
        ArgumentCaptor<UserModel> captor = ArgumentCaptor.forClass(UserModel.class);
        UserModel user = new UserModel();
        user.setUsername("user");
        user.setEnabled(false);

        when(this.userRepository.findUserByUsername(anyString())).thenReturn(user);

        //run
        this.userService.activateUser(MethodOrderer.Random.RANDOM_SEED_PROPERTY_NAME);

        verify(this.userRepository, times(1)).save(captor.capture());
        UserModel actual = captor.getValue();
        //assert
        assertEquals(user.getUsername(), actual.getUsername());
        assertTrue(actual.isEnabled());
    }

    @Test
    void getCurrentUser() {
    }
}
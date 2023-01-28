package com.todolist.service;

import com.todolist.entity.UserModel;
import com.todolist.entity.dto.UserCreationDTO;
import com.todolist.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.security.auth.login.CredentialNotFoundException;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
        UserModel result = userService.addUser(userDTO);

        assert(user.getUsername().equals(result.getUsername()));


    }

    @Test
    void shouldNotAddUser(){
        UserModel user = new UserModel();
    }

    @Test
    void findUserByUsername() {
    }

    @Test
    void findAllUsersByUsernames() {
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
    void activateUser() {
    }

    @Test
    void getCurrentUser() {
    }
}
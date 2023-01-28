package com.todolist.service;

import com.todolist.entity.EventModel;
import com.todolist.entity.TaskModel;
import com.todolist.entity.UserModel;
import com.todolist.entity.dto.PublicUserDTO;
import com.todolist.entity.dto.TaskDTO;
import com.todolist.repository.EventRepository;
import com.todolist.repository.TaskRepository;
import com.todolist.repository.UserRepository;
import com.todolist.security.authentication.UsernamePasswordAuthToken;
import com.todolist.security.userdetails.UserDetailsImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;



import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class TaskModelServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    TaskRepository taskRepository;
    @Mock
    EventRepository eventRepository;
    @InjectMocks
    UserService userService;
    @InjectMocks
    TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        taskService = new TaskService(taskRepository, eventRepository, userService);

        UsernamePasswordAuthToken token = mock(UsernamePasswordAuthToken.class);
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        UserModel user = new UserModel();
        user.setId(UUID.randomUUID());
        user.setUsername("user");
        user.setRoles("USER");
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(userDetails);
        when(userService.getCurrentUser()).thenReturn(user);
    }

    @AfterEach
    void tearDown(){

    }

    @Test
    void shouldFindTasksByUser() {

        //setup
        UserModel testUser = new UserModel();
        testUser.setId(UUID.randomUUID());
        testUser.setUsername("user");

        UserModel otherUser = new UserModel();
        otherUser.setUsername("wrong");

        TaskModel testTask1 = new TaskModel();
        testTask1.setTitle("Test task 1");
        testTask1.setOwnerUser(testUser);

        TaskModel testTask2 = new TaskModel();
        testTask2.setTitle("Test task 2");
        testTask2.setOwnerUser(otherUser);

        List<TaskModel> taskModelList = List.of(testTask1, testTask2);



        when(taskRepository.findTasksByOwnerUser(any(UserModel.class))).
                thenReturn(taskModelList.stream()
                        .filter(taskModel -> taskModel.getOwnerUser().equals(testUser))
                        .collect(Collectors.toList()));
        //query
        List<TaskDTO> actualList = this.taskService.findTasksByUser();

        //assert
        assertEquals(actualList.size(), 1);
        assertEquals(actualList.get(0).getOwner().getUsername(), testUser.getUsername());

    }

    @Test
    void shouldAddTask() {

        //setup

        EventModel event = new EventModel();
        event.setTitle("testEvent");
        event.setId(UUID.randomUUID());

        TaskModel task1 = new TaskModel();
        task1.setTitle("task1");
        task1.setEventId(event.getId());

        TaskModel task2 = new TaskModel();
        task2.setTitle("task2");

        when(eventRepository.findEventById(any(UUID.class))).thenReturn(event);
        when(taskRepository.save(task1)).thenReturn(task1);
        when(taskRepository.save(task2)).thenReturn(task2);

        //query
        TaskModel actual1 = this.taskService.addTask(task1);
        TaskModel actual2 = this.taskService.addTask(task2);

        //assert
        assertAll(
                () -> assertEquals(task2.getTitle(), actual2.getTitle()),
                () -> assertNull(actual2.getEventName()),
                () -> assertEquals(task1.getTitle(), actual1.getTitle()),
                () -> assertEquals(actual1.getEventName(), event.getTitle())
        );

    }

    @Test
    void shouldUpdateTask() {
        //setup
        UserModel user = new UserModel();
        user.setUsername("test");

        TaskModel oldTask = new TaskModel();
        oldTask.setId(UUID.randomUUID());
        oldTask.setOwnerUser(user);

        TaskModel newTask = new TaskModel();
        newTask.setTitle("new");
        newTask.setDate(new Date());
        newTask.setComplete(true);
        newTask.setDescription("new");
        newTask.setColor("green");

        when(this.taskRepository.findTaskById(any(UUID.class))).thenReturn(oldTask);
        when(this.taskRepository.save(any(TaskModel.class))).thenReturn(oldTask);
        //query

        TaskDTO updatedTask = this.taskService.updateTask(oldTask.getId(), newTask);

        //assert
        assertAll(
                () -> assertEquals(updatedTask.getTitle(), "new"),
                () -> assertTrue(updatedTask.isComplete()),
                () -> assertEquals(updatedTask.getDescription(), "new"),
                () -> assertEquals(updatedTask.getColor(), "green")
        );
    }

    @Test
    void shouldMoveTask() {
        //setup
        UserModel user = new UserModel();
        user.setUsername("test");

        TaskModel task = new TaskModel();
        task.setId(UUID.randomUUID());
        task.setOwnerUser(user);

        when(this.taskRepository.findTaskById(any(UUID.class))).thenReturn(task);
        when(this.taskRepository.save(any(TaskModel.class))).thenReturn(task);

        //query
        TaskDTO updatedTask = this.taskService.moveTask(task.getId(), 10, 100);

        //assert
        assertAll(
                () -> assertEquals(updatedTask.getxLocation(), 10),
                () -> assertEquals(updatedTask.getyLocation(), 100)
        );
    }

    @Test
    void shouldCompleteTask() {
        //setup
        UserModel user = this.userService.getCurrentUser();

        TaskModel task = new TaskModel();
        task.setId(UUID.randomUUID());
        task.setComplete(false);
        task.setOwnerUser(user);

        when(this.taskRepository.findTaskById(any(UUID.class))).thenReturn(task);
        when(this.taskRepository.save(any(TaskModel.class))).thenReturn(task);

        //query
        TaskDTO updatedTask = this.taskService.completeTask(task.getId(), true);

        //assert
        assertTrue(updatedTask.isComplete());

    }



    @Test
    void shouldAssignUsersToTask() {
        //setup
        UserModel owner = this.userService.getCurrentUser();

        Set<UserModel> userModelSet = new HashSet<>();
        Set<String> usernameSet = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            UserModel user = new UserModel();
            user.setUsername("user" + i);
            usernameSet.add("user" + i);
            userModelSet.add(user);
        }

        TaskModel task = new TaskModel();
        task.setId(UUID.randomUUID());
        task.setOwnerUser(owner);

        when(this.taskRepository.findTaskById(any(UUID.class))).thenReturn(task);
        when(this.taskRepository.save(any(TaskModel.class))).thenReturn(task);
        when(this.userService.findAllUsersByUsernames(usernameSet)).thenReturn(userModelSet);
        //query

        TaskDTO taskDTO = this.taskService.assignUsersToTask(task.getId(), usernameSet);

        //assert
        assertEquals(taskDTO.getAssignedUsers().size(), usernameSet.size());
        assertTrue(taskDTO.getAssignedUsers().stream()
                .map(PublicUserDTO::getUsername).anyMatch("user3"::equals));

    }

}
package com.todolist.service;

import com.todolist.model.Task;
import com.todolist.model.User;
import com.todolist.repository.EventRepository;
import com.todolist.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TaskServiceTest {


    @Mock
    TaskRepository taskRepository;
    @Mock
    EventRepository eventRepository;
    @Mock
    UserService userService;
    TaskService taskService;
        User testUser = new User();
    @BeforeEach
    void setUp() {
        taskService = new TaskService(taskRepository, eventRepository, userService);
        testUser.setUsername("testUser");
        testUser.setPassword("123");
        testUser.setRoles("USER");
        testUser.setEmail("asdsd@asd.asd");
        testUser.setEnabled(true);
    }

    @Test
    void addTaskWithEvent() {
    }

    @Test
    void updateTask() {
    }

    @Test
    void moveTask() {
    }

    @Test
    void completeTask() {
    }

    @Test
    void findTaskByDate() {
    }

    @Test
    void findTasksByUser() {

        //setup
        Task testTask = new Task();
        testTask.setTitle("Test task");
        testTask.setDate(new Date());
        testTask.setDescription("This is a test task");
        testTask.setComplete(false);
        testTask.setUser(testUser);

        //query



    }

    @Test
    void findTasksByUserWhereEventNull() {
    }

    @Test
    void findTasksByEvent() {
    }

    @Test
    void deleteTask() {
    }

    @Test
    void findUserTasksWithAssignedUsernamesAndEventId() {
    }

    @Test
    void assignUsernamesToTasks() {
    }

    @Test
    void assignUsersToTask() {
    }

    @Test
    void isUserTaskCreatorOrAssignedToTask() {
    }

    @Test
    void isUserTaskCreator() {
    }
}
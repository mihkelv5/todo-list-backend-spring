package com.todolist.service;

import com.todolist.model.TaskModel;
import com.todolist.model.UserModel;
import com.todolist.repository.EventRepository;
import com.todolist.repository.TaskRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;

@DataJpaTest
class TaskModelServiceTest {


    @Mock
    TaskRepository taskRepository;
    @Mock
    EventRepository eventRepository;
    @InjectMocks
    UserService userService;
    private AutoCloseable autoCloseable;
    TaskService taskService;
        UserModel testUser = new UserModel();
    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        taskService = new TaskService(taskRepository, eventRepository, userService);
        testUser.setUsername("testUser");
        testUser.setPassword("123");
        testUser.setRoles("USER");
        testUser.setEmail("asdsd@asd.asd");
        testUser.setEnabled(true);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void findTasksByUser() {

        //setup
        TaskModel testTask = new TaskModel();
        testTask.setTitle("Test task");
        testTask.setDate(new Date());
        testTask.setDescription("This is a test task");
        testTask.setComplete(false);
        testTask.setUser(testUser);

        //query



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
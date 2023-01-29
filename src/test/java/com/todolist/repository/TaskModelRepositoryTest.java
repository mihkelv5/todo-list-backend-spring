package com.todolist.repository;

import com.todolist.entity.EventModel;
import com.todolist.entity.TaskModel;
import com.todolist.entity.UserModel;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Sql(scripts = {"classpath:tasks.sql"})
class TaskModelRepositoryTest {

    @Autowired
    private TaskRepository testTaskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;
    UserModel user = new UserModel();
    EventModel event = new EventModel();


    @BeforeAll
    void setup(){

        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("email@email.ee");
        this.userRepository.save(user);

        event.setTitle("event");
        event.setDescription("event");
        this.eventRepository.save(event); //to get id
    }


    @Test
    void itShouldAddTask() {
        //setup
        TaskModel testTask = new TaskModel();
        testTask.setTitle("Test task");
        testTask.setDate(new Date());
        testTask.setDescription("This is a test task");
        testTask.setComplete(false);

        //query
        TaskModel addedTask = testTaskRepository.save(testTask);

        //assert
        assertNotNull(addedTask);
        assertEquals(addedTask.getTitle(), testTask.getTitle());
    }


    @Test
    void itShouldFindTaskById(){
        //setup
        UUID existingTaskId = this.testTaskRepository.findAll().get(0).getId();

        //query
        TaskModel task = this.testTaskRepository.findTaskById(existingTaskId);

        //assert
        assertNotNull(task);
    }

    @Test
    void itShouldFindTaskBetweenDates() throws ParseException {
        //setup
            //dates for querying
        Date afterDate = new SimpleDateFormat("dd/MM/yyyy").parse("10/10/2010");
        Date beforeDate1 = new SimpleDateFormat("dd/MM/yyyy").parse("20/10/2010");
        Date beforeDate2 = new SimpleDateFormat("dd/MM/yyyy").parse("30/10/2010");


        List<TaskModel> tasks = this.testTaskRepository.findAll();
        assertEquals(tasks.size(), 2);

             //set task date that is between dates
        Date taskDateBetween = new SimpleDateFormat("dd/MM/yyyy").parse("15/10/2010");
        tasks.get(0).setDate(taskDateBetween);

            //set task date that is after dates
        Date taskDateAfter = new SimpleDateFormat("dd/MM/yyyy").parse("25/10/2010");
        tasks.get(1).setDate(taskDateAfter);

            //save tasks to db
        this.testTaskRepository.save(tasks.get(0));
        this.testTaskRepository.save(tasks.get(1));
        //query

        List<TaskModel> dateBetweenTasks1 = this.testTaskRepository.findTasksBetweenDates(beforeDate1, afterDate);
        List<TaskModel> dateBetweenTasks2 = this.testTaskRepository.findTasksBetweenDates(beforeDate2, afterDate);
        //assert

        assertEquals(dateBetweenTasks1.size(), 1);
        assertEquals(dateBetweenTasks2.size(), 2);

    }

    @Test
    void itShouldAssignUserToTask() {

        //setup
        UUID eventId = this.eventRepository.findAll().get(0).getId();
        UserModel user = this.userRepository.findByUsername("username");

        TaskModel task = this.testTaskRepository.findAll().get(0);
        Set<UserModel> users = new HashSet<>();
        users.add(user);

        task.setAssignedUsers(users);
        task.setEventId(event.getId());
        task.setEventName(event.getTitle());
        this.testTaskRepository.save(task);

        //query
        TaskModel actualTask = this.testTaskRepository.findTasksByAssignedUsersAndEventId(user, eventId).get(0);

        //assert
        assertEquals(task.getTitle(), actualTask.getTitle());

    }




}
package com.todolist.repository;

import com.todolist.entity.EventModel;
import com.todolist.entity.TaskModel;
import com.todolist.entity.UserModel;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

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
        this.user = this.userRepository.save(user);

        event.setTitle("event");
        event.setDescription("event");
        this.event = this.eventRepository.save(event); //to get id
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
        TaskModel task = this.testTaskRepository.findAll().get(0);
        Set<UserModel> users = new HashSet<>();
        users.add(user);

        task.setAssignedUsers(users);
        task.setEventId(event.getId());
        task.setEventName(event.getTitle());
        this.testTaskRepository.save(task);

        //query
        TaskModel actualTask = this.testTaskRepository.findTasksByAssignedUsersAndEventId(this.user, this.event.getId()).get(0);

        //assert
        assertEquals(task.getTitle(), actualTask.getTitle());

    }
    @Test
    void shouldFindTasksByOwner(){
        //setup
        List<TaskModel> tasks = this.testTaskRepository.findAll();
        tasks.get(0).setOwnerUser(this.user);
        this.testTaskRepository.saveAll(tasks);

        //query
        List<TaskModel> actualTasks = this.testTaskRepository.findTasksByOwnerUser(this.user);

        //assert
        assertEquals(1, actualTasks.size());
        assertEquals(actualTasks.get(0).getOwnerUser().getUsername(), this.user.getUsername());

    }
    @Test
    void shouldFindTasksByOwnerWithoutEvent() {
        //setup
            //all tasks are registered to the owner, but only one is assigned to event. Goal is to get tasks that are user private tasks.
        List<TaskModel> tasks = this.testTaskRepository.findAll();
        tasks.forEach(task -> task.setOwnerUser(this.user));
        tasks.get(0).setEventId(this.event.getId());
        this.testTaskRepository.saveAll(tasks);

        //query
        List<TaskModel> actualTasks = this.testTaskRepository.findTasksByOwnerUserAndEventIdIsNull(this.user);

        //assert
        assertEquals( 1, actualTasks.size());
        assertNull(actualTasks.get(0).getEventId());
        assertEquals(actualTasks.get(0).getOwnerUser().getUsername(), this.user.getUsername());
    }

    @Test
    void shouldFindTasksByEventId() {
        //setup
        List<TaskModel> tasks = this.testTaskRepository.findAll();
        tasks.get(0).setEventId(this.event.getId());
        this.testTaskRepository.saveAll(tasks);

        //query
        List<TaskModel> actualTasks = this.testTaskRepository.findTasksByEventId(this.event.getId());

        //assert
        assertEquals(1, actualTasks.size());
        assertEquals(this.event.getId(), actualTasks.get(0).getEventId());
    }

    @Test
    void shouldDeleteTasksByEventId() {
        //setup
        List<TaskModel> tasksWithEvent = new ArrayList<>();
            //create 10 tasks, half of them will have events assigned to them
        for (int i = 0; i < 10; i++) {
            TaskModel task = new TaskModel();
            task.setTitle("task" + i);
            task.setComplete(false);
            if(i % 2 == 0){
                task.setEventId(this.event.getId());
            }
            tasksWithEvent.add(task);
        }
        this.testTaskRepository.saveAll(tasksWithEvent);

        //query
        this.testTaskRepository.deleteTasksByEventId(this.event.getId());
        List<TaskModel> actualTasks = this.testTaskRepository.findAll();

        actualTasks.forEach(task -> assertNull(task.getEventId())); //none of the events should have task assigned to them.
    }


    @Test
    void shouldExistByIdAndAssignedUserId(){ //technically could be tested in previous tests, but I think it is good practice to test everything separately.
        //setup
        TaskModel task = this.testTaskRepository.findAll().get(0);
        Set<UserModel> users = new HashSet<>();
        users.add(this.user);
        task.setAssignedUsers(users); //saved set cant be immutable, so has to be done in this way. (?)
        task = this.testTaskRepository.save(task); //to get its id;

        //query
        boolean doesActuallyExist = this.testTaskRepository.existsTaskByIdAndAssignedUsersId(task.getId(), this.user.getId());

        //assert
        assertTrue(doesActuallyExist);
    }

    @Test
    void shouldExistByIdAndOwnerUserId(){ //technically could be tested in previous tests, but I think it is good practice to test everything separately.
        //setup
        TaskModel task = this.testTaskRepository.findAll().get(0);

        task.setOwnerUser(this.user); //saved set cant be immutable, so has to be done in this way. (?)
        task = this.testTaskRepository.save(task); //to get its id;

        //query
        boolean doesActuallyExist = this.testTaskRepository.existsTaskByIdAndOwnerUserId(task.getId(), this.user.getId());

        //assert
        assertTrue(doesActuallyExist);
    }






}
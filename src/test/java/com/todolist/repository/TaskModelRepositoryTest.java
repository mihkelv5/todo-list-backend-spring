package com.todolist.repository;

import com.todolist.entity.TaskModel;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class TaskModelRepositoryTest {

    @Autowired
    private TaskRepository testTaskRepository;



    @AfterEach
    void deleteAll(){
        testTaskRepository.deleteAll();
    }


    @Test
    void itShouldAddTask() {
        //add task
        TaskModel testTask = new TaskModel();
        testTask.setTitle("Test task");
        testTask.setDate(new Date());
        testTask.setDescription("This is a test task");
        testTask.setComplete(false);

        UUID addedTaskId = testTaskRepository.save(testTask).getId();

        TaskModel newTask = testTaskRepository.findTaskById(addedTaskId);
        assertNotNull(newTask);
    }




}
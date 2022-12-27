package com.todolist.repository;

import com.todolist.model.Task;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
class TaskRepositoryTest {

    @Autowired
    private TaskRepository testTaskRepository;



    @AfterEach
    void deleteAll(){
        testTaskRepository.deleteAll();
    }


    @Test
    void itShouldAddTask() {
        //add task
        Task testTask = new Task();
        testTask.setTitle("Test task");
        testTask.setDate(new Date());
        testTask.setDescription("This is a test task");
        testTask.setComplete(false);

        Long addedTaskId = testTaskRepository.save(testTask).getId();

        Task newTask = testTaskRepository.findTaskById(addedTaskId);
        assertNotNull(newTask);
    }




}
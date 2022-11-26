package com.todolist.task;

import com.todolist.user.User;
import com.todolist.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskrepository;
    private final UserRepository userRepository;


    @Autowired
    public TaskService(TaskRepository taskrepository, UserRepository userRepository) {
        this.taskrepository = taskrepository;
        this.userRepository = userRepository;
    }



    public Task findTaskById(Long id) {
        return taskrepository.findTaskById(id);
    }


    Task addTask(Task task){
        return taskrepository.save(task);
    }

    Task moveTask(Task task){
        return taskrepository.save(task);
    }

    List<Task> findAllTasks() {
        return taskrepository.findAll();
    }

    List<Task> findTaskByDate(Date date) {
        return taskrepository.findTasksByDate(date);
    }


    List<Task> findTasksByEvent(String event) {
        return taskrepository.findTasksByEvent(event);
    }

    @Transactional
    void deleteTask(Long id) throws IOException {
        taskrepository.deleteTaskById(id);
    }



}

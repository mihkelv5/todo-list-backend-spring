package com.todolist.service;

import com.todolist.model.Event;
import com.todolist.model.Task;
import com.todolist.model.User;
import com.todolist.repository.EventRepository;
import com.todolist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskrepository;
    private final EventRepository eventRepository;



    @Autowired
    public TaskService(TaskRepository taskrepository, EventRepository eventRepository) {
        this.taskrepository = taskrepository;
        this.eventRepository = eventRepository;
    }



    public Task findTaskById(Long id) {
        return taskrepository.findTaskById(id);
    }


    public Task addTask(Task task, User user){
        task.setUser(user);
        return taskrepository.save(task);
    }

    public Task addTaskWithEvent(Long eventId, Task task){
        task.setEventId(eventId);
        return taskrepository.save(task);
    }

    public Task moveTask(Long id, int xLocation, int yLocation){
        Task task = this.taskrepository.findTaskById(id);
        task.setCoordinates(xLocation, yLocation);
        return this.taskrepository.save(task);
    }

    public List<Task> findAllTasks() {
        return taskrepository.findAll();
    }

    public List<Task> findTaskByDate(Date date) {
        return taskrepository.findTasksByDate(date);
    }

    public List<Task> findTasksByUser(User user) {
        return taskrepository.findTasksByUser(user);
    }
    public List<Task> findTasksByEvent(Long eventId) {
        return taskrepository.findTasksByEventId(eventId);
    }

    @Transactional
    public void deleteTask(Long id) throws IOException {
        taskrepository.deleteTaskById(id);
    }



}

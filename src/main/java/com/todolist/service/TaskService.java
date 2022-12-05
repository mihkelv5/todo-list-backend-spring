package com.todolist.service;

import com.todolist.model.Event;
import com.todolist.model.Task;
import com.todolist.model.User;
import com.todolist.repository.EventRepository;
import com.todolist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final EventRepository eventRepository;



    @Autowired
    public TaskService(TaskRepository taskrepository, EventRepository eventRepository) {
        this.taskRepository = taskrepository;

        this.eventRepository = eventRepository;
    }



    public Task findTaskById(Long id) {
        return taskRepository.findTaskById(id);
    }


    public Task addTask(Task task, User user){
        task.setUser(user);
        return taskRepository.save(task);
    }

    public Task addTaskWithEvent(Long eventId, Task task, User user){
        Event event = this.eventRepository.findEventById(eventId);
        task.setEventName(event.getTitle());
        task.setEventId(event.getId());
        task.setUser(user);
        return taskRepository.save(task);
    }

    public Task updateTask(Long dataBaseTaskId, Task updatedTask) {
        Task task = taskRepository.findTaskById(dataBaseTaskId);
        task.setTitle(updatedTask.getTitle());
        task.setDate(updatedTask.getDate());
        task.setComplete(updatedTask.isComplete());
        task.setDescription(updatedTask.getDescription());
        task.setColor(updatedTask.getColor());
        return taskRepository.save(task);

    }

    public Task moveTask(Long id, int xLocation, int yLocation){
        Task task = this.taskRepository.findTaskById(id);
        task.setCoordinates(xLocation, yLocation);
        return this.taskRepository.save(task);
    }



    public Task completeTask(Long id, Boolean isComplete){
        Task task = taskRepository.findTaskById(id);
        if(task != null){
            task.setComplete(isComplete);
            taskRepository.save(task);

        }
        return task;
    }

    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> findTaskByDate(Date date) {
        return taskRepository.findTasksByDate(date);
    }

    public List<Task> findTasksByUser(User user) {
        return taskRepository.findTasksByUser(user);
    }
    public List<Task> findTasksByUserWhereEventNull(User user){
        return taskRepository.findTasksByUserAndEventIdIsNull(user);
    }

    public List<Task> findTasksByEvent(Long eventId) {
        return taskRepository.findTasksByEventId(eventId);
    }

    @Transactional
    public void deleteTask(Long id) {
        taskRepository.deleteTaskById(id);
    }


}

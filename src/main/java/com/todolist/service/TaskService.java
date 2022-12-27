package com.todolist.service;

import com.todolist.model.Event;
import com.todolist.model.Task;
import com.todolist.model.User;
import com.todolist.repository.EventRepository;
import com.todolist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final EventRepository eventRepository;

    private final UserService userService;



    @Autowired
    public TaskService(TaskRepository taskrepository, EventRepository eventRepository, UserService userService) {
        this.taskRepository = taskrepository;
        this.eventRepository = eventRepository;
        this.userService = userService;
    }



    public Task findTaskById(Long id) {
        return taskRepository.findTaskById(id);
    }


    public Task addTask(Task task, User user){
        task.setUser(user);
        return taskRepository.save(task);
    }

    public Task addTaskWithEvent(Long eventId, Task task, User user){ //TODO: merge with add task method.
        Event event = this.eventRepository.findEventById(eventId);
        task.setEventName(event.getTitle());
        task.setEventId(event.getId());
        task.setUser(user);
        return taskRepository.save(task);
    }

    public Task updateTask(Long taskId, Task updatedTask) {
        Task task = taskRepository.findTaskById(taskId);
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



    public List<Task> findTaskByDate(Date date) {
        return taskRepository.findTasksByDate(date);
    }

    public List<Task> findTasksByUser(User user) {
        List<Task> tasks = taskRepository.findTasksByUser(user);
        return assignUsernamesToTasks(tasks);
    }
    public List<Task> findTasksByUserWhereEventNull(User user){
        List<Task> tasks =  taskRepository.findTasksByUserAndEventIdIsNull(user);
        return assignUsernamesToTasks(tasks);
    }

    public List<Task> findTasksByEvent(Long eventId) {
        List<Task> tasks = taskRepository.findTasksByEventId(eventId);
        return assignUsernamesToTasks(tasks);
    }

    @Transactional
    public void deleteTask(Long taskId) {
        taskRepository.deleteTaskById(taskId);
    }


    public List<Task> findUserTasksWithAssignedUsernamesAndEventId(User user, Long eventId){
        List<Task> tasks = this.taskRepository.findTasksByAssignedUsersAndEventId(user, eventId);
        return assignUsernamesToTasks(tasks);
    }

    public List<Task> assignUsernamesToTasks(List<Task> tasks){
        tasks.forEach(task -> {
            task.setAssignedUsernames(task.getAssignedUsers().stream().map(User::getUsername).collect(Collectors.toSet()));
            task.setOwnerUsername(task.getUser().getUsername());
        });
        return tasks;
    }

    public Task assignUsersToTask(Long taskId, List<String> usernames) {
        Task task = this.taskRepository.findTaskById(taskId);
        Set<String> usernameSet = new HashSet<>(usernames);
        Set<User> userSet = this.userService.findAllUsersByUsernameSet(usernameSet);
        task.setAssignedUsers(userSet);
        this.taskRepository.save(task);
        return task;
    }

    public boolean isUserTaskCreatorOrAssignedToTask(Long taskId){
        User user = this.userService.getCurrentUser();
        return this.taskRepository.existsTaskByIdAndUserOrIdAndAssignedUsers(taskId, user, taskId, user);
    }

    public boolean isUserTaskCreator(Long taskId) {
        User user = this.userService.getCurrentUser();
        return this.taskRepository.existsTaskByIdAndUser(taskId, user);
    }
}

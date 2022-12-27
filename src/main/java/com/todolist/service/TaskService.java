package com.todolist.service;

import com.todolist.model.EventModel;
import com.todolist.model.TaskModel;
import com.todolist.model.UserModel;
import com.todolist.repository.EventRepository;
import com.todolist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.*;
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



    public TaskModel findTaskById(UUID taskId) {
        return taskRepository.findTaskById(taskId);
    }


    public TaskModel addTask(TaskModel task, UserModel user){
        task.setUser(user);
        return taskRepository.save(task);
    }

    public TaskModel addTaskWithEvent(Long eventId, TaskModel task, UserModel user){ //TODO: merge with add task method.
        EventModel event = this.eventRepository.findEventById(eventId);
        task.setEventName(event.getTitle());
        task.setEventId(event.getId());
        task.setUser(user);
        return taskRepository.save(task);
    }

    public TaskModel updateTask(UUID taskId, TaskModel updatedTask) {
        TaskModel task = taskRepository.findTaskById(taskId);
        task.setTitle(updatedTask.getTitle());
        task.setDate(updatedTask.getDate());
        task.setComplete(updatedTask.isComplete());
        task.setDescription(updatedTask.getDescription());
        task.setColor(updatedTask.getColor());
        return taskRepository.save(task);

    }

    public TaskModel moveTask(UUID taskId, int xLocation, int yLocation){
        TaskModel task = this.taskRepository.findTaskById(taskId);
        task.setCoordinates(xLocation, yLocation);
        return this.taskRepository.save(task);
    }



    public TaskModel completeTask(UUID taskId, Boolean isComplete){
        TaskModel task = taskRepository.findTaskById(taskId);
        if(task != null){
            task.setComplete(isComplete);
            taskRepository.save(task);
        }
        return task;
    }



    public List<TaskModel> findTaskByDate(Date date) {
        return taskRepository.findTasksByDate(date);
    }

    public List<TaskModel> findTasksByUser(UserModel user) {
        List<TaskModel> tasks = taskRepository.findTasksByUser(user);
        return assignUsernamesToTasks(tasks);
    }
    public List<TaskModel> findTasksByUserWhereEventNull(UserModel user){
        List<TaskModel> tasks =  taskRepository.findTasksByUserAndEventIdIsNull(user);
        return assignUsernamesToTasks(tasks);
    }

    public List<TaskModel> findTasksByEvent(Long eventId) {
        List<TaskModel> tasks = taskRepository.findTasksByEventId(eventId);
        return assignUsernamesToTasks(tasks);
    }

    @Transactional
    public void deleteTask(UUID taskId) {
        taskRepository.deleteTaskById(taskId);
    }


    public List<TaskModel> findUserTasksWithAssignedUsernamesAndEventId(UserModel user, Long eventId){
        List<TaskModel> tasks = this.taskRepository.findTasksByAssignedUsersAndEventId(user, eventId);
        return assignUsernamesToTasks(tasks);
    }

    public List<TaskModel> assignUsernamesToTasks(List<TaskModel> tasks){
        tasks.forEach(task -> {
            task.setAssignedUsernames(task.getAssignedUsers().stream().map(UserModel::getUsername).collect(Collectors.toSet()));
            task.setOwnerUsername(task.getUser().getUsername());
        });
        return tasks;
    }

    public TaskModel assignUsersToTask(UUID taskId, List<String> usernames) {
        TaskModel task = this.taskRepository.findTaskById(taskId);
        Set<String> usernameSet = new HashSet<>(usernames);
        Set<UserModel> userSet = this.userService.findAllUsersByUsernameSet(usernameSet);
        task.setAssignedUsers(userSet);
        this.taskRepository.save(task);
        return task;
    }

    public boolean isUserTaskCreatorOrAssignedToTask(UUID taskId){
        UserModel user = this.userService.getCurrentUser();
        return this.taskRepository.existsTaskByIdAndUserOrIdAndAssignedUsers(taskId, user, taskId, user);
    }

    public boolean isUserTaskCreator(UUID taskId) {
        UserModel user = this.userService.getCurrentUser();
        return this.taskRepository.existsTaskByIdAndUser(taskId, user);
    }
}

package com.todolist.service;

import com.todolist.entity.EventModel;
import com.todolist.entity.TaskModel;
import com.todolist.entity.UserModel;
import com.todolist.entity.dto.TaskDTO;
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


    //CREATE methods
    public TaskModel addTask(TaskModel task){
        UserModel user = this.userService.getCurrentUser();
        if(task.getEventId() != null) { //if task is created for an event, sets the event title
            EventModel event = this.eventRepository.findEventById(task.getEventId());
            task.setEventName(event.getTitle());
        }
        task.setOwnerUser(user);
        return taskRepository.save(task);
    }

    //READ methods
    public TaskModel findTaskById(UUID taskId) {
        return taskRepository.findTaskById(taskId);
    }

    public List<TaskModel> findTaskByDate(Date date) {
        return taskRepository.findTasksByDate(date);
    }

    public Set<TaskDTO> findTasksByUser() {
        UserModel user = this.userService.getCurrentUser();
        Set<TaskModel> tasks = taskRepository.findTasksByOwnerUser(user);
        return tasks.stream().map(TaskDTO::TaskDTOConverter).collect(Collectors.toSet());
    }
    public Set<TaskDTO> findTasksByUserWhereEventNull(){
        UserModel user = this.userService.getCurrentUser();
        Set<TaskModel> tasks =  taskRepository.findTasksByOwnerUserAndEventIdIsNull(user);
        return tasks.stream().map(TaskDTO::TaskDTOConverter).collect(Collectors.toSet());
    }

    public Set<TaskDTO> findTasksByEvent(UUID eventId) {
        List<TaskModel> tasks = taskRepository.findTasksByEventId(eventId);
        return tasks.stream().map(TaskDTO::TaskDTOConverter).collect(Collectors.toSet());
    }

    public Set<TaskDTO> findUserTasksWithAssignedUsernamesAndEventId(UUID eventId){
        UserModel user = this.userService.getCurrentUser();
        Set<TaskModel> tasks = this.taskRepository.findTasksByAssignedUsersAndEventId(user, eventId);
        return tasks.stream().map(TaskDTO::TaskDTOConverter).collect(Collectors.toSet());
    }

    //READ methods for security
    public boolean isUserTaskCreatorOrAssignedToTask(UUID taskId, UUID userId){

        return this.taskRepository.existsTaskByIdAndOwnerUserIdOrIdAndAssignedUsersId(taskId, userId, taskId, userId);
    }

    public boolean isUserTaskCreator(UUID taskId, UUID userId) {

        return this.taskRepository.existsTaskByIdAndOwnerUserId(taskId, userId);
    }

    //UPDATE methods
    public TaskModel updateTask(UUID taskId, TaskModel updatedTask) {
        TaskModel task = taskRepository.findTaskById(taskId);
        task.setTitle(updatedTask.getTitle());
        task.setDate(updatedTask.getDate());
        task.setComplete(updatedTask.isComplete());
        task.setDescription(updatedTask.getDescription());
        task.setColor(updatedTask.getColor());
        return taskRepository.save(task);

    }

    public TaskDTO moveTask(UUID taskId, int xLocation, int yLocation){
        TaskModel task = this.taskRepository.findTaskById(taskId);
        task.setCoordinates(xLocation, yLocation);
        return TaskDTO.TaskDTOConverter(this.taskRepository.save(task));
    }

    public TaskDTO completeTask(UUID taskId, Boolean isComplete){
        TaskModel task = taskRepository.findTaskById(taskId);
        if(task != null){
            task.setComplete(isComplete);
            taskRepository.save(task);
        }
        return TaskDTO.TaskDTOConverter(task);
    }

    public TaskDTO assignUsersToTask(UUID taskId, List<String> usernames) {
        TaskModel task = this.taskRepository.findTaskById(taskId);
        Set<String> usernameSet = new HashSet<>(usernames);
        Set<UserModel> userSet = this.userService.findAllUsersByUsernames(usernameSet);
        task.setAssignedUsers(userSet);
        return TaskDTO.TaskDTOConverter(this.taskRepository.save(task));
    }



    //DELETE methods
    @Transactional
    public void deleteTask(UUID taskId) {
        taskRepository.deleteTaskById(taskId);
    }



}

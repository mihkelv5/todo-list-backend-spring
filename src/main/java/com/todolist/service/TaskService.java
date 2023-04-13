package com.todolist.service;

import com.todolist.entity.dto.PublicUserDTO;
import com.todolist.entity.event.EventModel;
import com.todolist.entity.task.TaskModel;
import com.todolist.entity.user.UserModel;
import com.todolist.entity.dto.TaskDTO;
import com.todolist.repository.EventRepository;
import com.todolist.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
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
    private final ProfilePictureService profilePictureService;



    @Autowired
    public TaskService(TaskRepository taskrepository, EventRepository eventRepository, UserService userService, ProfilePictureService profilePictureService) {
        this.taskRepository = taskrepository;
        this.eventRepository = eventRepository;
        this.userService = userService;
        this.profilePictureService = profilePictureService;
    }


    //CREATE methods
    public TaskDTO addTask(TaskModel task){
        UserModel user = this.userService.getCurrentUser();
        if(task.getEventId() != null) { //if task is created for an event, sets the event title
            EventModel event = this.eventRepository.findEventById(task.getEventId());
            task.setEventName(event.getTitle());
        }
        task.setOwnerUser(user);
        return this.taskDTOConverter(taskRepository.save(task));
    }

    //READ methods
    public TaskModel findTaskById(UUID taskId) {
        return taskRepository.findTaskById(taskId);
    }

    public TaskDTO findTaskDTOById(UUID taskId) {
        return this.taskDTOConverter(taskRepository.findTaskById(taskId));
    }

    public List<TaskModel> findTaskByDates(Date before, Date after) {
        return taskRepository.findTasksBetweenDates(before, after);
    }

    public List<TaskDTO> findTasksByUser() {
        UserModel user = this.userService.getCurrentUser();
        List<TaskModel> tasks = taskRepository.findTasksByOwnerUser(user);
        return this.taskDTOListConverter(tasks);
    }
    public List<TaskDTO> findTasksByUserWhereEventNull(){
        UserModel user = this.userService.getCurrentUser();
        List<TaskModel> tasks = taskRepository.findTasksByOwnerUserAndEventIdIsNull(user);
        return this.taskDTOListConverter(tasks);
    }

    public List<TaskDTO> findTasksByEvent(UUID eventId) {
        List<TaskModel> tasks = taskRepository.findTasksByEventId(eventId);
        return this.taskDTOListConverter(tasks);
    }

    public List<TaskDTO> findUserTasksWithAssignedUsernamesAndEventId(UUID eventId){
        UserModel user = this.userService.getCurrentUser();
        List<TaskModel> tasks = this.taskRepository.findTasksByAssignedUsersAndEventId(user, eventId);
        return this.taskDTOListConverter(tasks);
    }

    //READ methods for security
    public boolean isUserTaskCreatorOrAssignedToTask(UUID taskId, UUID userId){
        return this.taskRepository.existsTaskByIdAndAssignedUsersId(taskId, userId);
    }

    public boolean isUserTaskCreator(UUID taskId, UUID userId) {
        return this.taskRepository.existsTaskByIdAndOwnerUserId(taskId, userId);
    }

    //UPDATE methods
    public TaskDTO updateTask(UUID taskId, TaskModel updatedTask) {
        TaskModel task = taskRepository.findTaskById(taskId);
        task.setTitle(updatedTask.getTitle());
        task.setDate(updatedTask.getDate());
        task.setComplete(updatedTask.isComplete());
        task.setDescription(updatedTask.getDescription());
        task.setColor(updatedTask.getColor());
        task.setTags(updatedTask.getTags());
        return this.taskDTOConverter(taskRepository.save(task));

    }

    public TaskDTO moveTask(UUID taskId, int xLocation, int yLocation){
        TaskModel task = this.taskRepository.findTaskById(taskId);
        task.setCoordinates(xLocation, yLocation);
        return this.taskDTOConverter(this.taskRepository.save(task));
    }

    public TaskDTO completeTask(UUID taskId, Boolean isComplete){
        TaskModel task = taskRepository.findTaskById(taskId);
        if(task == null){
            throw new EntityNotFoundException("Task with id " + taskId + " not found");
        }
        task.setComplete(isComplete);
        taskRepository.save(task);
        return this.taskDTOConverter(task);
    }

    public TaskDTO assignUsersToTask(UUID taskId, Set<String> usernames) {
        TaskModel task = this.taskRepository.findTaskById(taskId);
        Set<UserModel> userSet = this.userService.findAllUsersByUsernames(usernames);
        task.setAssignedUsers(userSet);
        TaskDTO taskDTO = this.taskDTOConverter(this.taskRepository.save(task));
        taskDTO.getAssignedUsers().forEach(user -> user.setImageString(this.profilePictureService.getUserImage(user.getUsername())));
        return taskDTO;
    }



    //DELETE methods
    @Transactional
    public void deleteTask(UUID taskId) {
        taskRepository.deleteTaskById(taskId);
    }



    public TaskDTO taskDTOConverter(TaskModel task) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(task.getId());
        taskDTO.setTitle(task.getTitle());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setDate(task.getDate());
        taskDTO.setComplete(task.isComplete());
        if (task.getEventId() != null) {
            taskDTO.setEventId(task.getEventId());
            taskDTO.setEventName(task.getEventName());
        }
        taskDTO.setxLocation(task.getxLocation());
        taskDTO.setyLocation(task.getyLocation());
        taskDTO.setColor(task.getColor());
        taskDTO.setOwner(this.userService.publicUserDTOConverter(task.getOwnerUser()));
        taskDTO.setAssignedUsers(this.userService.publicUserDTOSetConverter(task.getAssignedUsers()));
        taskDTO.setTags(task.getTags());
        return taskDTO;
    }

    public List<TaskDTO> taskDTOListConverter(List<TaskModel> taskModelList){
        List<TaskDTO> taskDTOList = new ArrayList<>();
        taskModelList.forEach(task -> taskDTOList.add(this.taskDTOConverter(task)));
        return taskDTOList;
    }


}

package com.todolist.controller;

import com.todolist.model.TaskModel;
import com.todolist.model.UserModel;
import com.todolist.service.TaskService;
import com.todolist.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping(path = "/api/task")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }


    @GetMapping("/user/all")
    public ResponseEntity<List<TaskModel>> getTasksByUser(){
        UserModel user = userService.getCurrentUser();
        List<TaskModel> tasks = taskService.findTasksByUser(user);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/user/private")
    public ResponseEntity<List<TaskModel>> getTasksByUserWhereEventNull(){
        UserModel user = userService.getCurrentUser();
        List<TaskModel> tasks = taskService.findTasksByUserWhereEventNull(user);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PutMapping("/assign/{taskId}")
    @PreAuthorize("@preAuthFilter.checkIfUserCreatedTask(#taskId)")
    public ResponseEntity<TaskModel> assignUsersToTask(@PathVariable UUID taskId, @RequestBody List<String> usernames){
        TaskModel task = this.taskService.assignUsersToTask(taskId, usernames);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/event/{eventId}/user")
    @PreAuthorize("@preAuthFilter.checkIfUserInEvent(#eventId)")
    public ResponseEntity<List<TaskModel>> getTasksByAssignedUserWithEvent(@PathVariable UUID eventId){
        UserModel user = this.userService.getCurrentUser();
        List<TaskModel> tasks = this.taskService.findUserTasksWithAssignedUsernamesAndEventId(user, eventId); //fix that username and eventId positions aren't changed
        return ResponseEntity.ok(tasks);
    }


    @GetMapping("/find/{taskId}")
    @PreAuthorize("@preAuthFilter.checkIfUserInTask(#taskId)")
    public ResponseEntity<TaskModel> getTaskById(@PathVariable("taskId") UUID taskId){
        TaskModel task = taskService.findTaskById(taskId);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }


    @GetMapping("/event/{eventId}")
    @PreAuthorize("@preAuthFilter.checkIfUserInEvent(#eventId)")
    public ResponseEntity<List<TaskModel>> getTasksByEvent(@PathVariable("eventId") UUID eventId){
        List<TaskModel> taskList = taskService.findTasksByEvent(eventId);
        return new ResponseEntity<>(taskList, HttpStatus.OK);
    }

    @PostMapping("/add/event/{eventId}")
    @PreAuthorize("@preAuthFilter.checkIfUserInEvent(#eventId)")
    public ResponseEntity<?> addTaskWithEvent(@PathVariable("eventId") UUID eventId, @RequestBody TaskModel task){
        UserModel user = userService.getCurrentUser();
        TaskModel newTask = this.taskService.addTaskWithEvent(eventId, task, user);
        return new ResponseEntity<>(newTask, HttpStatus.CREATED);
    }

    @PostMapping("/add")
    public ResponseEntity<TaskModel> addTask(@RequestBody TaskModel task){
        try {
            UserModel user = userService.getCurrentUser();
            TaskModel newTask = taskService.addTask(task, user);
            return new ResponseEntity<>(newTask, HttpStatus.CREATED);

        }catch (NullPointerException e){
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


    @PutMapping("/moveTask/{taskId}")
    @PreAuthorize("@preAuthFilter.checkIfUserCanMoveTask(#taskId)")
    public ResponseEntity<TaskModel> moveTaskById(@PathVariable UUID taskId, @RequestBody int[] coordinates){
        TaskModel newTask = taskService.moveTask(taskId, coordinates[0], coordinates[1]);
        return new ResponseEntity<>(newTask, HttpStatus.OK);
    }
    @Transactional
    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable("taskId") UUID taskId) {
        taskService.deleteTask(taskId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PutMapping("/update/{taskId}")
    @PreAuthorize("@preAuthFilter.checkIfUserCreatedTask(#taskId)")
    public ResponseEntity<TaskModel> updateTask(@PathVariable("taskId") UUID taskId, @RequestBody TaskModel task){
        TaskModel updatePost = taskService.updateTask(taskId, task);
        return new ResponseEntity<>(updatePost, HttpStatus.OK);
    }

    @PutMapping("/complete/{taskId}")
    @PreAuthorize("@preAuthFilter.checkIfUserInTask(#taskId)")
    public ResponseEntity<TaskModel> completeTaskById(@PathVariable("taskId") UUID taskId, @RequestBody boolean isComplete){
        TaskModel newTask = this.taskService.completeTask(taskId, isComplete);
        return ResponseEntity.ok(newTask);
    }




}

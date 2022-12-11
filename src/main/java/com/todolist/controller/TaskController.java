package com.todolist.controller;

import com.todolist.model.Task;
import com.todolist.model.User;
import com.todolist.service.TaskService;
import com.todolist.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.transaction.Transactional;
import java.util.List;



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
    public ResponseEntity<List<Task>> getTasksByUser(){
        User user = userService.getCurrentUser();
        List<Task> tasks = taskService.findTasksByUser(user);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/user/private")
    public ResponseEntity<List<Task>> getTasksByUserWhereEventNull(){
        User user = userService.getCurrentUser();
        List<Task> tasks = taskService.findTasksByUserWhereEventNull(user);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PutMapping("/assign/{taskId}")
    @PreAuthorize("@preAuthFilter.checkIfUserCreatedTask(#taskId)")
    public ResponseEntity<Task> assignUsersToTask(@PathVariable Long taskId, @RequestBody List<String> usernames){
        Task task = this.taskService.assignUsersToTask(taskId, usernames);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/event/{eventId}/user")
    @PreAuthorize("@preAuthFilter.checkIfUserInEvent(#eventId)")
    public ResponseEntity<List<Task>> getTasksByAssignedUserWithEvent(@PathVariable Long eventId){
        User user = this.userService.getCurrentUser();
        List<Task> tasks = this.taskService.findUserTasksWithAssignedUsernamesAndEventId(user, eventId); //fix that username and eventId positions aren't changed
        return ResponseEntity.ok(tasks);
    }


    @GetMapping("/find/{taskId}")
    @PreAuthorize("@preAuthFilter.checkIfUserInTask(#taskId)")
    public ResponseEntity<Task> getTaskById(@PathVariable("taskId") Long taskId){
        Task task = taskService.findTaskById(taskId);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }


    @GetMapping("/event/{eventId}")
    @PreAuthorize("@preAuthFilter.checkIfUserInEvent(#eventId)")
    public ResponseEntity<List<Task>> getTasksByEvent(@PathVariable("eventId") Long eventId){
        List<Task> taskList = taskService.findTasksByEvent(eventId);
        return new ResponseEntity<>(taskList, HttpStatus.OK);
    }

    @PostMapping("/add/event/{eventId}")
    @PreAuthorize("@preAuthFilter.checkIfUserInEvent(#eventId)")
    public ResponseEntity<?> addTaskWithEvent(@PathVariable("eventId") Long eventId, @RequestBody Task task){
        User user = userService.getCurrentUser();
        task.setId(null);
        Task newTask = this.taskService.addTaskWithEvent(eventId, task, user);
        return new ResponseEntity<>(newTask, HttpStatus.CREATED);
    }

    @PostMapping("/add")
    public ResponseEntity<Task> addTask(@RequestBody Task task){
        try {
            User user = userService.getCurrentUser();
            task.setId(null);
            Task newTask = taskService.addTask(task, user);
            return new ResponseEntity<>(newTask, HttpStatus.CREATED);

        }catch (NullPointerException e){
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


    @PutMapping("/moveTask/{taskId}")
    @PreAuthorize("@preAuthFilter.checkIfUserCanMoveTask(#taskId)")
    public ResponseEntity<Task> moveTaskById(@PathVariable Long taskId, @RequestBody int[] coordinates){
        Task newTask = taskService.moveTask(taskId, coordinates[0], coordinates[1]);
        return new ResponseEntity<>(newTask, HttpStatus.OK);
    }
    @Transactional
    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable("taskId") Long taskId) {
        taskService.deleteTask(taskId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PutMapping("/update/{taskId}")
    @PreAuthorize("@preAuthFilter.checkIfUserCreatedTask(#taskId)")
    public ResponseEntity<Task> updateTask(@PathVariable("taskId") Long taskId, @RequestBody Task task){
        Task updatePost = taskService.updateTask(taskId, task);
        return new ResponseEntity<>(updatePost, HttpStatus.OK);
    }

    @PutMapping("/complete/{taskId}")
    @PreAuthorize("@preAuthFilter.checkIfUserInTask(#taskId)")
    public ResponseEntity<Task> completeTaskById(@PathVariable("taskId") Long taskId, @RequestBody boolean isComplete){
        Task newTask = this.taskService.completeTask(taskId, isComplete);
        return ResponseEntity.ok(newTask);
    }




}

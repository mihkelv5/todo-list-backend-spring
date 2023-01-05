package com.todolist.controller;

import com.todolist.model.TaskModel;
import com.todolist.model.UserModel;
import com.todolist.service.TaskService;
import com.todolist.service.UserService;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.*;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping(path = "/api/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    //CREATE methods
    @PostMapping("/add")
    public ResponseEntity<TaskModel> addTask(@RequestBody TaskModel task){
        try {
            TaskModel newTask = taskService.addTask(task);
            return ResponseEntity.ok(newTask);

        }catch (NullPointerException e){
            return ResponseEntity.unprocessableEntity().build();
        }
    }




    //READ Methods
    @GetMapping("/user/all")
    public ResponseEntity<List<TaskModel>> getTasksByUser(){
        List<TaskModel> tasks = taskService.findTasksByUser();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/user/private")
    public ResponseEntity<List<TaskModel>> getTasksByUserWhereEventNull(){

        List<TaskModel> tasks = taskService.findTasksByUserWhereEventNull();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/event/{eventId}/user")
    @PreAuthorize("@preAuthFilter.checkIfUserInEvent(#eventId)")
    public ResponseEntity<List<TaskModel>> getTasksByAssignedUserWithEvent(@PathVariable UUID eventId, Authorization authorization){

        List<TaskModel> tasks = this.taskService.findUserTasksWithAssignedUsernamesAndEventId(eventId); //fix that username and eventId positions aren't changed
        return ResponseEntity.ok(tasks);
    }


    @GetMapping("/find/{taskId}")
    @PreAuthorize("@preAuthFilter.checkIfUserInTask(#taskId)")
    public ResponseEntity<TaskModel> getTaskById(@PathVariable("taskId") UUID taskId){
        TaskModel task = taskService.findTaskById(taskId);
        return ResponseEntity.ok(task);
    }


    @GetMapping("/event/{eventId}")
    @PreAuthorize("@preAuthFilter.checkIfUserInEvent(#eventId)")
    public ResponseEntity<List<TaskModel>> getTasksByEvent(@PathVariable("eventId") UUID eventId){
        List<TaskModel> taskList = taskService.findTasksByEvent(eventId);
        return ResponseEntity.ok(taskList);
    }



    //UPDATE methods
    @PutMapping("/assign/{taskId}/event/{eventId}")
    @PreAuthorize("@preAuthFilter.checkIfUserCreatedTask(#taskId)")
    @PreFilter(filterTarget = "usernames", value = "@preAuthFilter.usernamesInEvent(filterObject, #eventId)")
    public ResponseEntity<TaskModel> assignUsersToTask(@PathVariable UUID taskId, @PathVariable UUID eventId, @RequestBody List<String> usernames){
        //usernames.forEach(System.out::println);
        TaskModel task = this.taskService.assignUsersToTask(taskId, usernames);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/moveTask/{taskId}")
    @PreAuthorize("@preAuthFilter.checkIfUserCanMoveTask(#taskId)")
    public ResponseEntity<TaskModel> moveTaskById(@PathVariable UUID taskId, @RequestBody int[] coordinates){
        TaskModel newTask = taskService.moveTask(taskId, coordinates[0], coordinates[1]);
        return ResponseEntity.ok(newTask);
    }

    @PutMapping("/update/{taskId}")
    @PreAuthorize("@preAuthFilter.checkIfUserCreatedTask(#taskId)")
    public ResponseEntity<TaskModel> updateTask(@PathVariable("taskId") UUID taskId, @RequestBody TaskModel task){
        TaskModel updatePost = taskService.updateTask(taskId, task);
        return ResponseEntity.ok(updatePost);
    }

    @PutMapping("/complete/{taskId}")
    @PreAuthorize("@preAuthFilter.checkIfUserInTask(#taskId)")
    public ResponseEntity<TaskModel> completeTaskById(@PathVariable("taskId") UUID taskId, @RequestBody boolean isComplete){
        TaskModel newTask = this.taskService.completeTask(taskId, isComplete);
        return ResponseEntity.ok(newTask);
    }



    //DELETE methods
    @Transactional
    @DeleteMapping("/delete/{taskId}")
    @PreAuthorize("@preAuthFilter.checkIfUserCreatedTask(#taskId)")
    public ResponseEntity<?> deleteTask(@PathVariable("taskId") UUID taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok().build();
    }






}

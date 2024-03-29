package com.todolist.controller;

import com.todolist.entity.task.TaskModel;
import com.todolist.entity.dto.TaskDTO;
import com.todolist.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.*;

import jakarta.transaction.Transactional;

import java.util.*;


@RestController
@RequestMapping(path = "/api/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    //CREATE methods
    @PostMapping("/add")
    public ResponseEntity<TaskDTO> addTask(@RequestBody TaskModel task){
        try {
            TaskDTO newTask = taskService.addTask(task);
            return ResponseEntity.ok(newTask);

        }catch (NullPointerException e){
            return ResponseEntity.unprocessableEntity().build();
        }
    }




    //READ Methods
    @GetMapping("/user/all")
    public ResponseEntity<List<TaskDTO>> getTasksByUser(){
        List<TaskDTO> tasks = taskService.findTasksByUser();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/user/private")
    public ResponseEntity<List<TaskDTO>> getTasksByUserWhereEventNull(){

        List<TaskDTO> tasks = taskService.findTasksByUserWhereEventNull();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/event/{eventId}/user")
    @PreAuthorize("@preAuthMethodFilter.checkIfUserInEvent(#eventId)")
    public ResponseEntity<List<TaskDTO>> getTasksByAssignedUserWithEvent(@PathVariable UUID eventId){

        List<TaskDTO> tasks = this.taskService.findUserTasksWithAssignedUsernamesAndEventId(eventId); //fix that username and eventId positions aren't changed
        return ResponseEntity.ok(tasks);
    }


    @GetMapping("/find/{taskId}")
    @PreAuthorize("@preAuthMethodFilter.checkIfUserInTask(#taskId)")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable("taskId") UUID taskId){
        TaskDTO task = this.taskService.findTaskDTOById(taskId);
        return ResponseEntity.ok(task);
    }


    @GetMapping("/event/{eventId}")
    @PreAuthorize("@preAuthMethodFilter.checkIfUserInEvent(#eventId)")
    public ResponseEntity<List<TaskDTO>> getTasksByEvent(@PathVariable("eventId") UUID eventId){
        List<TaskDTO> taskList = taskService.findTasksByEvent(eventId);
        return ResponseEntity.ok(taskList);
    }



    //UPDATE methods
    @PutMapping("/assign/{taskId}/event/{eventId}")
    @PreAuthorize("@preAuthMethodFilter.checkIfUserCreatedTask(#taskId)")
    @PreFilter(filterTarget = "usernames", value = "@preAuthMethodFilter.usernamesInEvent(filterObject, #eventId)")
    public ResponseEntity<TaskDTO> assignUsersToTask(@PathVariable UUID taskId, @PathVariable UUID eventId, @RequestBody Set<String> usernames){
        TaskDTO task = this.taskService.assignUsersToTask(taskId, usernames);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/moveTask/{taskId}")
    @PreAuthorize("@preAuthMethodFilter.checkIfUserCanMoveTask(#taskId)")
    public ResponseEntity<TaskDTO> moveTaskById(@PathVariable UUID taskId, @RequestBody int[] coordinates){
        TaskDTO newTask = taskService.moveTask(taskId, coordinates[0], coordinates[1]);
        return ResponseEntity.ok(newTask);
    }

    @PutMapping("/update/{taskId}")
    @PreAuthorize("@preAuthMethodFilter.checkIfUserCreatedTask(#taskId)")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable("taskId") UUID taskId, @RequestBody TaskModel task){
        TaskDTO updatedTask = taskService.updateTask(taskId, task);
        return ResponseEntity.ok(updatedTask);
    }

    @PutMapping("/complete/{taskId}")
    @PreAuthorize("@preAuthMethodFilter.checkIfUserInTask(#taskId)")
    public ResponseEntity<TaskDTO> completeTaskById(@PathVariable("taskId") UUID taskId, @RequestBody boolean isComplete){
        TaskDTO newTask = this.taskService.completeTask(taskId, isComplete);
        return ResponseEntity.ok(newTask);
    }



    //DELETE methods
    @Transactional
    @DeleteMapping("/delete/{taskId}")
    @PreAuthorize("@preAuthMethodFilter.checkIfUserCreatedTask(#taskId)")
    public ResponseEntity<?> deleteTask(@PathVariable("taskId") UUID taskId) {
        taskService.deleteTask(taskId);
        Map<String, String> response = new HashMap<>();
        response.put("response", "task deleted");
        return ResponseEntity.ok().body(response);
    }
}

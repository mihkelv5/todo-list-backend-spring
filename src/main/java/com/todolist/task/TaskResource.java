package com.todolist.task;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/api/task")
public class TaskResource {

    private final TaskService taskService;

    public TaskResource(TaskService taskService) {
        this.taskService = taskService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<Task>> getAllTasks(){
        List<Task> tasks = taskService.findAllTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Task> addTask(@RequestBody Task task){
        task.setId(null);
        Task newTask = taskService.addTask(task);
        System.out.println("addTask");
        return new ResponseEntity<>(newTask, HttpStatus.CREATED);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable("id") Long id){
        Task task = taskService.findTaskById(id);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @GetMapping("/findByEvent/{event}")
    public ResponseEntity<List<Task>> getTasksByEvent(@PathVariable("event") String event){
        List<Task> taskList = taskService.findTasksByEvent(event);
        return new ResponseEntity<>(taskList, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Task> updateTask(@RequestBody Task task){
        Task updatePost = taskService.addTask(task);
        return new ResponseEntity<>(updatePost, HttpStatus.OK);
    }

    @PutMapping("/moveTask")
    public ResponseEntity<Task> moveTaskById(@RequestBody Task task){
        Task newTask = taskService.moveTask(task);
        return new ResponseEntity<>(newTask, HttpStatus.OK);
    }

    @PutMapping("/complete/{id}")
    public ResponseEntity<Task> completeTaskById(@PathVariable Long id){
        Task task = taskService.findTaskById(id);
        task.setComplete(!task.isComplete());
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable("id") Long id) throws IOException {
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }





}

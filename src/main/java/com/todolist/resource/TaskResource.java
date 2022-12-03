package com.todolist.resource;

import com.todolist.model.Task;
import com.todolist.model.User;
import com.todolist.service.TaskService;
import com.todolist.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;



@RestController
@RequestMapping(path = "/api/task")
public class TaskResource {

    private final TaskService taskService;
    private final UserService userService;

    public TaskResource(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }


    @GetMapping("/user/{id}/all")
    public ResponseEntity<List<Task>> getTasksByUser(@PathVariable("id") Long id){
        User user = userService.findUserById(id);

        List<Task> tasks = taskService.findTasksByUser(user);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/user/{id}/own")
    public ResponseEntity<List<Task>> getTasksByUserWhereEventNull(@PathVariable("id") Long id){
        User user = userService.findUserById(id);
        List<Task> tasks = taskService.findTasksByUserWhereEventNull(user);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable("id") Long id){
        Task task = taskService.findTaskById(id);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }


    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<Task>> getTasksByEvent(@PathVariable("eventId") Long eventId){
        List<Task> taskList = taskService.findTasksByEvent(eventId);
        return new ResponseEntity<>(taskList, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Task> addTask(@RequestBody Task task){
        try {
            User user = userService.getCurrentUser();
            task.setId(null);
            Task newTask = taskService.addTask(task, user);
            System.out.println("addTask");
            return new ResponseEntity<>(newTask, HttpStatus.CREATED);

        }catch (NullPointerException e){
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/add/event/{eventId}")
    public ResponseEntity<?> addTaskWithEvent(@PathVariable("eventId") Long eventId, @RequestBody Task task){
        User user = userService.getCurrentUser();
        task.setId(null);
        Task newTask = this.taskService.addTaskWithEvent(eventId, task, user);
        return new ResponseEntity<Task>(newTask, HttpStatus.CREATED);
    }


    @PutMapping("/moveTask/{id}")
    public ResponseEntity<Task> moveTaskById(@PathVariable Long id, @RequestBody int[] coordinates){
        Task newTask = taskService.moveTask(id, coordinates[0], coordinates[1]);
        return new ResponseEntity<>(newTask, HttpStatus.OK);
    }
    @Transactional
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable("id") Long id) throws IOException {
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    //endpoints not used in front end

    @GetMapping("/all")
    public ResponseEntity<List<Task>> getAllTasks(){
        List<Task> tasks = taskService.findAllTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
    @PutMapping("/update")
    public ResponseEntity<Task> updateTask(@RequestBody Task task){
        User user = userService.getCurrentUser();
        Task updatePost = taskService.addTask(task, user);
        return new ResponseEntity<>(updatePost, HttpStatus.OK);
    }

    @PutMapping("/complete/{id}")
    public ResponseEntity<Task> completeTaskById(@PathVariable Long id){
        Task task = taskService.findTaskById(id);
        task.setComplete(!task.isComplete()); //bad implementation due to multiple requests may overwrite each other
        return new ResponseEntity<>(task, HttpStatus.OK);
    }
}

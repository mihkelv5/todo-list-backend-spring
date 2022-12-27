package com.todolist.security;

import com.todolist.model.TaskModel;
import com.todolist.service.EventInvitationService;
import com.todolist.service.TaskService;
import com.todolist.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import java.util.UUID;


@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class PreAuthFilter {

    private final UserService userService;
    private final EventInvitationService invitationService;
    private final TaskService taskService;

    public PreAuthFilter(UserService userService, EventInvitationService invitationService, TaskService taskService) {
        this.userService = userService;
        this.invitationService = invitationService;
        this.taskService = taskService;
    }


    public boolean checkIfUserInEvent(String eventId) {
        if(eventId.matches("[0-9]+")){
            return userService.isUserInEvent(Long.valueOf(eventId));
        }
        return false;
    }


    public boolean checkIfUserIsInvited(String invitationId) {
        if (invitationId.matches("[0-9]+")) {
            return this.invitationService.isInvitationValid(Long.valueOf(invitationId));
        }
        return false;
    }

    public boolean checkIfUserInTask(UUID taskId){

        return this.taskService.isUserTaskCreatorOrAssignedToTask(taskId);

    }

    public boolean checkIfUserCreatedTask(UUID taskId) {

            return this.taskService.isUserTaskCreator(taskId);


    }

    public boolean checkIfUserCanMoveTask(UUID taskId) {
        TaskModel task = this.taskService.findTaskById(taskId);
        return checkIfUserInTask(taskId) || checkIfUserInEvent(String.valueOf(task.getEventId()));
    }
}

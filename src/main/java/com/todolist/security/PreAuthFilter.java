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


    public boolean checkIfUserInEvent(UUID eventId) {
        return userService.isUserInEvent(eventId);

    }


    public boolean checkIfUserIsInvited(UUID invitationId) {

        return this.invitationService.isInvitationValid(invitationId);

    }

    public boolean checkIfUserInTask(UUID taskId){

        return this.taskService.isUserTaskCreatorOrAssignedToTask(taskId);

    }

    public boolean checkIfUserCreatedTask(UUID taskId) {

            return this.taskService.isUserTaskCreator(taskId);


    }

    public boolean checkIfUserCanMoveTask(UUID taskId) {
        TaskModel task = this.taskService.findTaskById(taskId);
        return checkIfUserInTask(taskId) || checkIfUserInEvent(task.getEventId());
    }
}

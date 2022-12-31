package com.todolist.security;

import com.todolist.model.TaskModel;
import com.todolist.principal.UserPrincipalImpl;
import com.todolist.service.EventInvitationService;
import com.todolist.service.EventService;
import com.todolist.service.TaskService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;


@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class PreAuthFilter {

    private final EventInvitationService invitationService;
    private final TaskService taskService;
    private final EventService eventService;

    public PreAuthFilter(EventInvitationService invitationService, TaskService taskService, EventService eventService) {

        this.invitationService = invitationService;
        this.taskService = taskService;
        this.eventService = eventService;
    }


    public boolean checkIfUserInEvent(UUID eventId) {
        UUID userId = this.getCurrentUserId();
        return this.eventService.isUserInEvent(eventId, userId);
    }


    public boolean checkIfUserIsInvited(UUID invitationId) {
        UUID userId = this.getCurrentUserId();
        return this.invitationService.isInvitationValid(invitationId, userId);

    }

    public boolean checkIfUserInTask(UUID taskId){
        UUID userId = this.getCurrentUserId();
        return this.taskService.isUserTaskCreatorOrAssignedToTask(taskId, userId);

    }

    public boolean checkIfUserCreatedTask(UUID taskId) {
        UUID userId = this.getCurrentUserId();
        return this.taskService.isUserTaskCreator(taskId, userId);
    }

    public boolean checkIfUserCanMoveTask(UUID taskId) {
        TaskModel task = this.taskService.findTaskById(taskId);
        return checkIfUserInTask(taskId) || checkIfUserInEvent(task.getEventId());
    }

    //helper method
    public UUID getCurrentUserId(){
        UserPrincipalImpl userDetails = (UserPrincipalImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getId();
    }
}

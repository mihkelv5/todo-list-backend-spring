package com.todolist.security.filter;

import com.todolist.model.TaskModel;
import com.todolist.security.userdetails.UserDetailsImpl;
import com.todolist.service.EventInvitationService;
import com.todolist.service.EventService;
import com.todolist.service.TaskService;
import com.todolist.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;


@Configuration
@EnableMethodSecurity()
public class PreAuthFilter {

    private final EventInvitationService invitationService;
    private final TaskService taskService;
    private final EventService eventService;

    private final UserService userService;

    public PreAuthFilter(EventInvitationService invitationService, TaskService taskService, EventService eventService, UserService userService) {

        this.invitationService = invitationService;
        this.taskService = taskService;
        this.eventService = eventService;
        this.userService = userService;
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

    public boolean usernamesInEvent (String username, UUID eventId){
        return this.userService.isUsernameInEvent(username, eventId);
    }


    //helper method
    public UUID getCurrentUserId(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getId();
    }

}

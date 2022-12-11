package com.todolist.security;

import com.todolist.service.EventInvitationService;
import com.todolist.service.TaskService;
import com.todolist.service.UserService;
import org.springframework.stereotype.Component;


@Component
public class PreAuthFilter {

    final
    UserService userService;
    final
    EventInvitationService invitationService;
    final
    TaskService taskService;

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

    public boolean checkIfUserInTask(String taskId){
        if(taskId.matches("[0-9]+")){
            return this.taskService.isUserTaskCreatorOrAssignedToTask(Long.valueOf(taskId));
        }
        return false;
    }

    public boolean checkIfUserCreatedTask(String taskId) {
        if(taskId.matches("[0-9]+")){
            return this.taskService.isUserTaskCreator(Long.valueOf(taskId));
        }
        return false;
    }
}

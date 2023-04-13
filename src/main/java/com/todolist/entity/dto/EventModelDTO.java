package com.todolist.entity.dto;

import java.util.Set;
import java.util.UUID;

public class EventModelDTO {
    private UUID id;
    private String title;
    private String description;
    private String[] taskTags;
    private Set<PublicUserDTO> eventUsers;

    private Set<PublicUserDTO> invitedUsers;
    public EventModelDTO() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<PublicUserDTO> getEventUsers() {
        return eventUsers;
    }

    public void setEventUsers(Set<PublicUserDTO> eventUsers) {
        this.eventUsers = eventUsers;
    }

    public Set<PublicUserDTO> getInvitedUsers() {
        return invitedUsers;
    }

    public void setInvitedUsers(Set<PublicUserDTO> invitedUsers) {
        this.invitedUsers = invitedUsers;
    }

    public String[] getTaskTags() {
        return taskTags;
    }

    public void setTaskTags(String taskTag) {
        if(taskTag == null){
            this.taskTags = new String[0];
        } else {
            this.taskTags = taskTag.split(", ");
        }
    }
}

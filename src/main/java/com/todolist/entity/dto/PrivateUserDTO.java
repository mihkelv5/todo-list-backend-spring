package com.todolist.entity.dto;

import java.util.UUID;

public class PrivateUserDTO {
    private UUID userId;
    private String email;

    private String[] taskTags;
    private PublicUserDTO publicUser;

    public PrivateUserDTO() {
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public PublicUserDTO getPublicUser() {
        return publicUser;
    }

    public void setPublicUser(PublicUserDTO publicUser) {
        this.publicUser = publicUser;
    }

    public String[] getTaskTags() {
        return taskTags;
    }

    public void setTaskTags(String taskTags) {
        if(taskTags == null) {
            this.taskTags = new String[0];
        } else {
            this.taskTags = taskTags.split(", ");
        }
    }
}

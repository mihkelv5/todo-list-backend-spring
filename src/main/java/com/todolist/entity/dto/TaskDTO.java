package com.todolist.entity.dto;

import com.todolist.entity.task.TaskModel;

import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class TaskDTO {

    private UUID id;
    private String title;
    private String description;
    private Date date;
    private boolean isComplete;
    private UUID eventId;
    private String eventName;
    private int xLocation;
    private int yLocation;
    private String color;

    private String[] tags;
    private PublicUserDTO owner;
    private Set<PublicUserDTO> assignedUsers;

    public TaskDTO() {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getxLocation() {
        return xLocation;
    }

    public void setxLocation(int xLocation) {
        this.xLocation = xLocation;
    }

    public int getyLocation() {
        return yLocation;
    }

    public void setyLocation(int yLocation) {
        this.yLocation = yLocation;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public PublicUserDTO getOwner() {
        return owner;
    }

    public void setOwner(PublicUserDTO owner) {
        this.owner = owner;
    }

    public Set<PublicUserDTO> getAssignedUsers() {
        return assignedUsers;
    }

    public void setAssignedUsers(Set<PublicUserDTO> assignedUsers) {
        this.assignedUsers = assignedUsers;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String tags) {
        if(tags == null){
            this.tags = new String[0];
        } else {
            this.tags = tags.split(", ");
        }
    }
}

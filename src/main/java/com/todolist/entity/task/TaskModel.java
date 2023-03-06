package com.todolist.entity.task;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.todolist.entity.user.UserModel;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name= "task")
public class TaskModel implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;
    private String title;
    private String description;
    @Temporal(TemporalType.DATE)
    private Date date;
    @Column(nullable = false)
    private boolean isComplete;
    private UUID eventId;
    private String eventName;
    private int xLocation;
    private int yLocation;
    private String color;

    private String tags;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private UserModel ownerUser;

    @ManyToMany
    @JsonIgnore
    private Set<UserModel> assignedUsers = new HashSet<>();

    public TaskModel() {}


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String task) {
        this.title = task;
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

    public void setEventId(UUID event_id) {
        this.eventId = event_id;
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

    public int getyLocation() {
        return yLocation;
    }

    public void setCoordinates(int xLocation, int yLocation) {
        this.xLocation = xLocation;
        this.yLocation = yLocation;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }



    public UserModel getOwnerUser() {
        return ownerUser;
    }

    public void setOwnerUser(UserModel user) {
        this.ownerUser = user;
    }

    public Set<UserModel> getAssignedUsers() {
        return assignedUsers;
    }

    public void setAssignedUsers(Set<UserModel> assignedUsers) {
        this.assignedUsers = assignedUsers;
    }


    public String getTags() {
        return tags;
    }

    public void setTags(String tag) {
        this.tags = tag;
    }
}

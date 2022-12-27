package com.todolist.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    private Long eventId;
    private String eventName;
    private int xLocation;
    private int yLocation;

    private String color;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private UserModel user;
    @Transient
    private String ownerUsername;

    @ManyToMany
    @JsonIgnore
    private Set<UserModel> assignedUsers = new HashSet<>();

    @Transient
    private Set<String> assignedUsernames = new HashSet<>();

    public TaskModel() {}

    public TaskModel(UUID id, String title, String description, Date date, boolean isComplete, Long eventId, int xLocation, int yLocation, String color) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.isComplete = isComplete;
        this.eventId = eventId;
        this.xLocation = xLocation; //TODO: locations should be saved in map in event and user classes instead.
        this.yLocation = yLocation;
        this.color = color;
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

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long event_id) {
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



    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public Set<UserModel> getAssignedUsers() {
        return assignedUsers;
    }

    public void setAssignedUsers(Set<UserModel> assignedUsers) {
        this.assignedUsers = assignedUsers;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public Set<String> getAssignedUsernames() {
        return assignedUsernames;
    }

    public void setAssignedUsernames(Set<String> assignedUsername) {
        this.assignedUsernames = assignedUsername;
    }
}

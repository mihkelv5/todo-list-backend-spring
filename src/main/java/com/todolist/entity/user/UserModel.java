package com.todolist.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.todolist.entity.event.EventInvitationModel;
import com.todolist.entity.event.EventModel;
import com.todolist.entity.task.TaskModel;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users") //renamed to users as h2 does not allow "user" - Testing purposes
public class UserModel implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private boolean enabled;
    private String roles;
    private Date joinDate;
    private Date LastActivity;
    private String taskTags;
    @JsonIgnore
    @OneToMany(mappedBy = "ownerUser")
    private Set<TaskModel> tasks = new HashSet<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "eventUsers")
    private Set<EventModel> events = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "invitedUser")
    private Set<EventInvitationModel> eventInvitations = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<Friendship> friends = new HashSet<>();

    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private ProfilePictureData profilePictureData;

    public UserModel(String username, String email) {
        this.username = username;
        this.email = email;
        this.joinDate = new Date();
    }



    public UserModel() {
        this.joinDate = new Date();
    }



    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public Set<TaskModel> getTasks() {
        return tasks;
    }

    public void setTasks(Set<TaskModel> tasks) {
        this.tasks = tasks;
    }

    public Set<EventModel> getEvents() {
        return events;
    }

    public void setEvents(Set<EventModel> events) {
        this.events = events;
    }

    public Set<EventInvitationModel> getEventInvitations() {
        return eventInvitations;
    }

    public void setEventInvitations(Set<EventInvitationModel> eventInvitations) {
        this.eventInvitations = eventInvitations;
    }

    public Set<Friendship> getFriends() {
        return friends;
    }

    public void setFriends(Set<Friendship> friends) {
        this.friends = friends;
    }

    public ProfilePictureData getProfilePictureData() {
        return profilePictureData;
    }

    public void setProfilePictureData(ProfilePictureData profilePictureData) {
        this.profilePictureData = profilePictureData;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public Date getLastActivity() {
        return LastActivity;
    }

    public void setLastActivity(Date lastActivity) {
        LastActivity = lastActivity;
    }

    public String getTaskTags() {
        return taskTags;
    }

    public void setTaskTags(String taskTags) {
        this.taskTags = taskTags;
    }
}

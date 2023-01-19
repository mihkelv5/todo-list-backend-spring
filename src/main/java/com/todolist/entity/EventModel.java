package com.todolist.entity;


import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "event")
public class EventModel {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;
    private String title;

    private String description;

    @ManyToMany
    @JoinTable(
            name = "user_event_registration",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )

    private Set<UserModel> eventUsers = new HashSet<>();

    public EventModel() {
    }

    public EventModel(UUID id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
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

    public Set<UserModel> getEventUsers() {
        return eventUsers;
    }

    public void setEventUsers(Set<UserModel> eventUsers) {
        this.eventUsers = eventUsers;
    }

    public void registerUserToEvent(UserModel user) {
        this.eventUsers.add(user);
    }

    public void removeUserFromEvent(UserModel user){
        this.eventUsers.remove(user);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

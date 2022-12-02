package com.todolist.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String title;

    @ManyToMany
    @JoinTable(
            name = "user_event_registration",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnore
    private Set<User> eventUsers = new HashSet<>();


    public Event() {
    }

    public Event(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<User> getEventUsers() {
        return eventUsers;
    }

    public void setEventUsers(Set<User> eventUsers) {
        this.eventUsers = eventUsers;
    }

    public void registerUserToEvent(User user) {
        this.eventUsers.add(user);
    }

    public void removeUserFromEvent(User user){
        this.eventUsers.remove(user);
    }
}

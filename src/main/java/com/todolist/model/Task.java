package com.todolist.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name= "Task")
public class Task implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private Long id;
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
    private User user;




    public Task() {}

    public Task(Long id, String title, String description, Date date, boolean isComplete, Long eventId, int xLocation, int yLocation, String color) {
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



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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



    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}

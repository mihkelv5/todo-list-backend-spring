package com.todolist.task;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
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
    private String event;
    private int xLocation;
    private int yLocation;

    private String color;




    public Task() {}

    public Task(Long id, String title, String description, Date date, boolean isComplete, String event, int xLocation, int yLocation, String color) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.isComplete = isComplete;
        this.event = event;
        this.xLocation = xLocation;
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

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
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
}

package com.todolist.entity.dto;

import com.todolist.entity.user.UserModel;
import com.todolist.service.ProfilePictureService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;

public class PublicUserDTO {

    private String username;
    private String joinDate;

    private Date lastActiveDate;
    private String imageString;
    private int groupsJoined;
    private int tasksCreated;
    private int tasksCompleted;
    private int activeTasks;
    public PublicUserDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(joinDate);
        this.joinDate = (cal.get(Calendar.DATE)  + "." + (cal.get(Calendar.MONTH)+1) + "." + cal.get(Calendar.YEAR));
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    public int getGroupsJoined() {
        return groupsJoined;
    }

    public void setGroupsJoined(int groupsJoined) {
        this.groupsJoined = groupsJoined;
    }

    public int getTasksCreated() {
        return tasksCreated;
    }

    public void setTasksCreated(int tasksCreated) {
        this.tasksCreated = tasksCreated;
    }

    public int getTasksCompleted() {
        return tasksCompleted;
    }

    public void setTasksCompleted(int tasksCompleted) {
        this.tasksCompleted = tasksCompleted;
    }

    public int getActiveTasks() {
        return activeTasks;
    }

    public void setActiveTasks(int activeTasks) {
        this.activeTasks = activeTasks;
    }

    public Date getLastActiveDate() {
        return lastActiveDate;
    }

    public void setLastActiveDate(Date lastActiveDate) {
        this.lastActiveDate = lastActiveDate;
    }
}

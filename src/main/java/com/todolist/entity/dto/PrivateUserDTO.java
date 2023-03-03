package com.todolist.entity.dto;

import com.todolist.entity.user.UserModel;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class PrivateUserDTO {
    private UUID userId;
    private String username;
    private String email;
    //etc
    private String imageString;
    private String joinDate;

    private int groupsJoined;
    private int tasksCreated;
    private int tasksCompleted;
    private int activeTasks;

    public PrivateUserDTO() {
    }

    public static PrivateUserDTO privateUserDTOConverter(UserModel user){
        PrivateUserDTO privateUserDTO = new PrivateUserDTO();
        privateUserDTO.setUserId(user.getId());
        privateUserDTO.setUsername(user.getUsername());
        privateUserDTO.setEmail(user.getEmail());
        privateUserDTO.setJoinDate(user.getJoinDate());
        return privateUserDTO;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
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
}

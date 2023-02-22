package com.todolist.entity.dto;

import com.todolist.entity.user.UserModel;

import java.util.UUID;

public class PrivateUserDTO {
    UUID userId;
    String username;
    String email;
    //etc


    public PrivateUserDTO() {
    }

    public static PrivateUserDTO privateUserDTOConverter(UserModel user){
        PrivateUserDTO privateUserDTO = new PrivateUserDTO();
        privateUserDTO.setUserId(user.getId());
        privateUserDTO.setUsername(user.getUsername());
        privateUserDTO.setEmail(user.getEmail());
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
}

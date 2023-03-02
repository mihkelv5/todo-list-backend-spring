package com.todolist.entity.dto;

import com.todolist.entity.user.UserModel;
import com.todolist.service.ProfilePictureService;
import org.springframework.beans.factory.annotation.Autowired;

public class PublicUserDTO {

    private String username;

    private String imageString;
    public PublicUserDTO() {
    }

    //static method so it would be possible to convert to a DTO before sending the data
    public static PublicUserDTO publicUserDTOConverter(UserModel user){
        PublicUserDTO publicUser = new PublicUserDTO();
        publicUser.setUsername(user.getUsername());
        return publicUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }
}

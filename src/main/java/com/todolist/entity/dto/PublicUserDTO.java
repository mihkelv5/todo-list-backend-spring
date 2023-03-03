package com.todolist.entity.dto;

import com.todolist.entity.user.UserModel;
import com.todolist.service.ProfilePictureService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;

public class PublicUserDTO {

    private String username;
    private String joinDate;

    private String imageString;
    public PublicUserDTO() {
    }

    //static method so it would be possible to convert to a DTO before sending the data
    public static PublicUserDTO publicUserDTOConverter(UserModel user){
        PublicUserDTO publicUser = new PublicUserDTO();
        publicUser.setUsername(user.getUsername());
        publicUser.setJoinDate(user.getJoinDate());
        return publicUser;
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
}

package com.todolist.entity.dto;

import com.todolist.entity.user.UserModel;
import org.springframework.core.io.ByteArrayResource;

public class PublicUserDTO {
    //for now only username will be visible

    private String username;

    private ByteArrayResource image;
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

    public byte[] getImage() {
        return image.getByteArray();
    }

    public void setImage(byte[] image) {
        this.image = new ByteArrayResource(image);
    }
}

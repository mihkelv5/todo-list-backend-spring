package com.todolist.entity.dto;

public class UserCreationDTO {

    private String username;
    private String email;
    private String password;

    public UserCreationDTO(String username, String email, String password) {

        this.username = username;
        this.email = email;
        this.password = password;
    }

    public UserCreationDTO() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

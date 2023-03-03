package com.todolist.entity.dto;

import com.todolist.entity.user.Friendship;

public class FriendshipDTO {

    private PublicUserDTO friend;
    private boolean accepted;
    private boolean blocked;


    public FriendshipDTO() {

    }

    public PublicUserDTO getFriend() {
        return friend;
    }

    public void setFriend(PublicUserDTO friend) {
        this.friend = friend;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isBlocked() {
        return blocked;
    }
    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
    //static method so it would be possible to convert to a DTO before sending the data
}

package com.todolist.entity.dto;

import com.todolist.entity.Friendship;

public class FriendshipDTO {

    private PublicUserDTO friend;
    private boolean accepted;
    private boolean blocked;


    public FriendshipDTO() {

    }

    public static FriendshipDTO friendshipDTOConverter(Friendship friendship, String currentUser){
        FriendshipDTO friendshipDTO = new FriendshipDTO();

        // friendships are not saved bidirectionally in the DB, must check one is friend and which is the user itself
        if(currentUser.equals(friendship.getUser().getUsername())){
            friendshipDTO.setFriend(PublicUserDTO.publicUserDTOConverter(friendship.getUser()));
        } else {
            friendshipDTO.setFriend(PublicUserDTO.publicUserDTOConverter(friendship.getUser()));
        }
        friendshipDTO.setAccepted(friendship.isAccepted());
        friendshipDTO.setBlocked(friendship.isBlocked());

        return friendshipDTO;
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

package com.todolist.repository;

import com.todolist.entity.Friendship;
import com.todolist.entity.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {



    boolean existsByUserAndFriend(UserModel user, UserModel friend);

    @Query("SELECT f1 from Friendship f1 WHERE f1.friend.username = :username AND f1.accepted OR f1.user.username = :username")
    Set<Friendship> findAllFriendsWhereUser(String username);

    @Query("SELECT f1 from Friendship f1 WHERE f1.user.username = :username AND f1.accepted")
    Set<Friendship> findAllFriendsWhereFriend(String username);


    @Query("Select f from Friendship f where f.user = :user1 AND f.friend = :user2 OR f.friend = :user1 AND f.user = :user2")
    Friendship findFriendship(UserModel user1, UserModel user2);


    @Query("SELECT f FROM Friendship f WHERE f.friend = :inviteReceiver AND f.user = :inviteSender")
    Friendship findFriendshipRequest(UserModel inviteReceiver, UserModel inviteSender);

}

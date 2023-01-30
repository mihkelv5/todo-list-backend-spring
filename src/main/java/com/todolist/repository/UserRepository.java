package com.todolist.repository;

import com.todolist.entity.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID> {


    @Query("select (count(u) > 0) from UserModel u where upper(u.email) = upper(?1)")
    boolean existsByUserEmail(String email);

    @Query("select (count(u) > 0) from UserModel u where upper(u.username) = upper(?1)")
    boolean existsUserByUsername(String username);

    @Query("select u from UserModel u where upper(u.username) = upper(?1)")
    UserModel findUserByUsername(String username);

    @Query("select u from UserModel u where upper(u.email) = upper(?1)")
    UserModel findUserByEmail(String email);

    UserModel findUserById(UUID id);


    Set<UserModel> findUsersByEventsId(UUID eventId);

    @Query("""
            select (count(u) > 0) from UserModel u inner join u.events events
            where upper(u.username) = upper(?1) and events.id = ?2""")
    boolean existsUserModelByUsernameAndEventsId(String username, UUID eventId);
    boolean existsUserModelByIdAndEventsId(UUID userId, UUID eventId);

    @Query("SELECT u FROM UserModel u WHERE :eventId NOT IN (SELECT eu.id FROM u.events eu) AND :eventId NOT IN (SELECT ei.eventId FROM u.eventInvitations ei)")
    Set<UserModel> findUsersNotInEventByEventId(@Param("eventId") UUID eventId);

    @Query("Select u FROM UserModel u Where u.username IN :usernames")
    Set<UserModel> findAllUsersByUsernameSet(Set<String> usernames);

    @Query("SELECT i.invitedUser FROM EventInvitationModel i WHERE i.eventId = :eventId")
    Set<UserModel> findAlreadyInvitedUsersByEventId(UUID eventId);
}

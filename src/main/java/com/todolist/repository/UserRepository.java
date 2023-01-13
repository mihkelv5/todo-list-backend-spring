package com.todolist.repository;

import com.todolist.entity.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID> {

    UserModel findByUsername(String username);

    UserModel findUserByEmail(String email);

    UserModel findUserById(UUID id);




    List<UserModel> findUsersByEventsId(UUID eventId);

    boolean existsUserModelByUsernameAndEventsId(String username, UUID eventId);
    @Query("SELECT u.username FROM UserModel u WHERE :eventId NOT IN (SELECT eu.id FROM u.events eu) AND :eventId NOT IN (SELECT ei.eventId FROM u.eventInvitations ei)")
    List<String> findUsersNotInEvent(@Param("eventId") UUID eventId);

    @Query("Select u FROM UserModel u Where u.username IN :usernames")
    Set<UserModel> findAllUsersByUsernameSet(Set<String> usernames);
}

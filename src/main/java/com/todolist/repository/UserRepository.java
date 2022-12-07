package com.todolist.repository;

import com.todolist.model.Event;
import com.todolist.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findUserById(Long id);

    @Query("SELECT username FROM User ORDER BY username")
    List<String> findAllUsernames();
    boolean existsUserByEventsIdAndId(Long eventId, Long userId);

    boolean existsUserByEventsIdAndUsername(Long eventId, String username);


    List<User> findUsersByEvents(Event event);
    @Query("SELECT u.username FROM User u WHERE :eventId NOT IN (SELECT eu.id FROM u.events eu) AND :eventId NOT IN (SELECT ei.eventId FROM u.eventInvitations ei)")
    List<String> findUsersNotInEvent(@Param("eventId") Long eventId);
//@Query("SELECT u.username FROM User u WHERE :eventId NOT IN (SELECT eu.id FROM u.events eu)")
}

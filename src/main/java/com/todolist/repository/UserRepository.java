package com.todolist.repository;

import com.todolist.model.Event;
import com.todolist.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findUserById(Long id);

    boolean existsUserByEventsIdAndId(Long eventId, Long userId);

    boolean existsUserByEventsIdAndUsername(Long eventId, String username);
    List<User> findUsersByEvents(Event event);

}

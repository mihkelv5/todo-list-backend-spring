package com.todolist.repository;

import com.todolist.model.Event;
import com.todolist.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {

    Event findEventById(Long eventId);

    List<Event> findEventsByEventUsersIn (Set<User> users);

    void deleteEventById (Long id);
}

package com.todolist.repository;

import com.todolist.model.EventModel;
import com.todolist.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface EventRepository extends JpaRepository<EventModel, Long> {

    EventModel findEventById(Long eventId);

    List<EventModel> findEventsByEventUsersIn(Set<UserModel> users);


    void deleteEventById (Long id);
}

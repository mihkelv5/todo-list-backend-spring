package com.todolist.repository;

import com.todolist.entity.EventModel;
import com.todolist.entity.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface EventRepository extends JpaRepository<EventModel, UUID> {

    EventModel findEventById(UUID eventId);

    List<EventModel> findEventsByEventUsersIn(Set<UserModel> users);
    void deleteEventById (UUID id);

    boolean existsEventModelByIdAndEventUsersId (UUID eventId, UUID userId);
}

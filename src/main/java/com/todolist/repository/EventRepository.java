package com.todolist.repository;

import com.todolist.entity.EventModel;
import com.todolist.entity.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<EventModel, UUID> {

    EventModel findEventById(UUID eventId);

    @Query("select e from EventModel e where :user in (select eu from e.eventUsers eu)")
    List<EventModel> findEventsByUser(UserModel user);
    void deleteEventById (UUID id);



    @Query("""
            select (count(e) > 0) from EventModel e inner join e.eventUsers eventUsers
            where e.id = :eventId and eventUsers.id = :userId""")
    boolean isUserInEvent(UUID eventId, UUID userId);
}

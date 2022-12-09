package com.todolist.repository;

import com.todolist.model.Task;
import com.todolist.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Task findTaskById(Long id);

    List<Task> findTasksByDate(Date date);

    List<Task> findTasksByUser(User user);

    List<Task> findTasksByUserAndEventIdIsNull(User user);

    List<Task> findTasksByEventId(Long id);

    void deleteTaskById(Long id);

    void deleteTasksByEventId(Long eventId);

    List<Task> findTasksByAssignedUsersAndEventId(User user, Long eventId);

}

package com.todolist.task;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Task findTaskById (Long id);

    List<Task> findTasksByDate (Date date);

    void deleteTaskById(Long id);
}

package com.todolist.repository;

import com.todolist.entity.task.TaskModel;
import com.todolist.entity.user.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<TaskModel, UUID> {

    TaskModel findTaskById(UUID taskId);

    @Query("Select t FROM TaskModel t WHERE t.date <= :before AND t.date >= :after")
    List<TaskModel> findTasksBetweenDates(Date before, Date after);

    List<TaskModel> findTasksByOwnerUser(UserModel user);

    List<TaskModel> findTasksByOwnerUserAndEventIdIsNull(UserModel user);

    List<TaskModel> findTasksByEventId(UUID eventId);

    void deleteTaskById(UUID taskId);

    void deleteTasksByEventId(UUID eventId);


    List<TaskModel> findTasksByAssignedUsersAndEventId(UserModel user, UUID eventId);


    boolean existsTaskByIdAndAssignedUsersId(UUID taskId, UUID assignedUserId);

    boolean existsTaskByIdAndOwnerUserId(UUID taskId, UUID userId);
}

package com.todolist.repository;

import com.todolist.entity.TaskModel;
import com.todolist.entity.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<TaskModel, UUID> {

    TaskModel findTaskById(UUID taskId);

    List<TaskModel> findTasksByDate(Date date);

    List<TaskModel> findTasksByOwnerUser(UserModel user);

    List<TaskModel> findTasksByOwnerUserAndEventIdIsNull(UserModel user);

    List<TaskModel> findTasksByEventId(UUID eventId);

    void deleteTaskById(UUID taskId);

    void deleteTasksByEventId(UUID eventId);

    List<TaskModel> findTasksByAssignedUsersAndEventId(UserModel user, UUID eventId);

    boolean existsTaskByIdAndOwnerUserIdOrIdAndAssignedUsersId(UUID taskId, UUID userId, UUID taskIdAgain, UUID assignedUserId);

    boolean existsTaskByIdAndOwnerUserId(UUID taskId, UUID userId);
}

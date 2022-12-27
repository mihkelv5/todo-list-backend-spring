package com.todolist.repository;

import com.todolist.model.TaskModel;
import com.todolist.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<TaskModel, UUID> {

    TaskModel findTaskById(UUID taskId);

    List<TaskModel> findTasksByDate(Date date);

    List<TaskModel> findTasksByUser(UserModel user);

    List<TaskModel> findTasksByUserAndEventIdIsNull(UserModel user);

    List<TaskModel> findTasksByEventId(Long eventId);

    void deleteTaskById(UUID taskId);

    void deleteTasksByEventId(Long eventId);

    List<TaskModel> findTasksByAssignedUsersAndEventId(UserModel user, Long eventId);

    boolean existsTaskByIdAndUserOrIdAndAssignedUsers(UUID taskId, UserModel user, UUID taskIdAgain, UserModel assignedUser);

    boolean existsTaskByIdAndUser(UUID taskId, UserModel user);
}

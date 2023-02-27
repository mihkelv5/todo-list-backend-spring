package com.todolist.repository;

import com.todolist.entity.user.ProfilePictureData;
import com.todolist.entity.user.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface ProfilePictureRepository extends JpaRepository<ProfilePictureData, UUID> {
    @Query("select p from ProfilePictureData p where p.user.username = ?1")
    ProfilePictureData findByUsername(String username);

    void deleteByUser(UserModel user);

    @Transactional
    @Modifying
    @Query("delete from ProfilePictureData p where p.user= ?1 AND NOT (p.id = ?2)")
    void deleteUsersOldPicture(UserModel user, UUID id);
}

package com.todolist.repository;

import com.todolist.entity.event.EventInvitationModel;
import com.todolist.entity.user.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface EventInvitationRepository extends JpaRepository<EventInvitationModel, UUID> {

    void deleteEventInvitationById(UUID id);



    void deleteEventInvitationModelByInvitedUserUsernameAndEventId(String username, UUID eventId);
    @Query("""
            select e from EventInvitationModel e
            where e.invitedUser = ?1 and e.expirationDate > ?2 and e.isAccepted = ?3""")
    List<EventInvitationModel> findValidEventInvitesByUser(UserModel user, Date date, boolean isAccepted);


    //to check if a user already has invite from the same event
    boolean existsByInvitedUserAndEventIdAndExpirationDateIsAfter(UserModel user, UUID eventId, Date date);

    EventInvitationModel findEventInvitationByIdAndExpirationDateIsAfter (UUID invitationId, Date date);

    @Transactional
    @Modifying
    @Query("delete from EventInvitationModel e where e.event.id = ?1")
    void deleteAllByEventId(UUID eventId);

    @Query("Select COUNT(ei) from EventInvitationModel ei where ei.id = :inviteId and ei.invitedUser.id = :userId and ei.expirationDate > :date")
    int isInviteValid(UUID userId, UUID inviteId, Date date);

}

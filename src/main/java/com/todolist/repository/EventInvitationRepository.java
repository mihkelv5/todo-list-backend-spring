package com.todolist.repository;

import com.todolist.model.EventInvitationModel;
import com.todolist.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface EventInvitationRepository extends JpaRepository<EventInvitationModel, UUID> {

    EventInvitationModel findEventInvitationById(UUID id);

    void deleteEventInvitationById(UUID id);
    List<EventInvitationModel> findAllByInvitedUserAndExpirationDateIsAfterAndIsAccepted(UserModel user, Date date, boolean isAccepted);

    List<EventInvitationModel> findAllEventInvitationsByIsAccepted(boolean isAccepted);

    boolean existsByInvitedUserAndEventIdAndExpirationDateIsAfter(UserModel user, UUID eventId, Date date);


    EventInvitationModel findEventInvitationByIdAndExpirationDateIsAfter (UUID invitationId, Date date);

    void deleteAllByEventId(UUID eventId);

    @Query("Select COUNT(ei) from EventInvitationModel ei where ei.id = :inviteId and ei.invitedUser.id = :userId and ei.expirationDate > :date")
    int isInviteValid(UUID userId, UUID inviteId, Date date);

}

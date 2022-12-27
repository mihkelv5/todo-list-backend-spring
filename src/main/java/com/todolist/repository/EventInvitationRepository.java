package com.todolist.repository;

import com.todolist.model.EventInvitationModel;
import com.todolist.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface EventInvitationRepository extends JpaRepository<EventInvitationModel, Long> {

    EventInvitationModel findEventInvitationById(Long id);

    void deleteEventInvitationById(Long id);
    List<EventInvitationModel> findAllByInvitedUserAndExpirationDateIsAfterAndIsAccepted(UserModel user, Date date, boolean isAccepted);

    List<EventInvitationModel> findAllEventInvitationsByIsAccepted(boolean isAccepted);

    boolean existsByInvitedUserAndEventIdAndExpirationDateIsAfter(UserModel user, Long eventId, Date date);


    EventInvitationModel findEventInvitationByIdAndExpirationDateIsAfter (Long eventId, Date date);

    void deleteAllByEventId(Long eventId);

    @Query("Select COUNT(ei) from EventInvitationModel ei where ei.id = :inviteId and ei.invitedUser.id = :userId and ei.expirationDate > :date")
    int isInviteValid(Long userId, Long inviteId, Date date);

}

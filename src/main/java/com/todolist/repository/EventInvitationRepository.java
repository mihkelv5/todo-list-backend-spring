package com.todolist.repository;

import com.todolist.model.EventInvitation;
import com.todolist.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface EventInvitationRepository extends JpaRepository<EventInvitation, Long> {

    EventInvitation findEventInvitationById(Long id);

    void deleteEventInvitationById(Long id);
    List<EventInvitation> findAllByInvitedUserAndExpirationDateIsAfterAndIsAccepted(User user, Date date, boolean isAccepted);

    List<EventInvitation> findAllEventInvitationsByIsAccepted(boolean isAccepted);

    boolean existsByInvitedUserAndEventIdAndExpirationDateIsAfter(User user, Long eventId, Date date);


    EventInvitation findEventInvitationByIdAndExpirationDateIsAfter (Long eventId, Date date);

    void deleteAllByEventId(Long eventId);

    @Query("Select COUNT(ei) from EventInvitation ei where ei.id = :inviteId and ei.invitedUser.id = :userId and ei.expirationDate > :date")
    int isInviteValid(Long userId, Long inviteId, Date date);

}

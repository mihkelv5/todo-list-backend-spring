package com.todolist.repository;

import com.todolist.model.EventInvitation;
import com.todolist.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface EventInvitationRepository extends JpaRepository<EventInvitation, Long> {

    public List<EventInvitation> findAllByInvitedUserAndExpirationDateIsAfterAndIsAccepted(User user, Date date, boolean isAccepted);

    public List<EventInvitation> findAllEventInvitationsByIsAccepted(boolean isAccepted);


    public EventInvitation findEventInvitationByIdAndExpirationDateIsAfter (Long eventId, Date date);

}

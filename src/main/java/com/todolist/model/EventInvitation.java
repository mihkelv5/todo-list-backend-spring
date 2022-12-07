package com.todolist.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "EventInvitation")
public class EventInvitation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String requesterUsername;
    @Column(nullable = false)
    private Long eventId;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @Column(nullable = false)
    private String eventName;
    @Column(nullable = false, name = "is_accepted")
    private boolean isAccepted;
    @Column(nullable = false)
    private boolean isBlocked;
    @Column(nullable = false)
    private Date expirationDate;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User invitedUser;

    public EventInvitation() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 14);
        this.expirationDate = calendar.getTime();
        this.isAccepted = false;
        this.isBlocked = false;
    }

    public EventInvitation(Long id, String requesterUsername, Long eventId, boolean isAccepted, boolean isRejected, Date expirationDate, User invitedUser) {
        this.id = id;
        this.requesterUsername = requesterUsername;
        this.eventId = eventId;
        this.isAccepted = isAccepted;
        this.isBlocked = isRejected;
        this.expirationDate = expirationDate;
        this.invitedUser = invitedUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequesterUsername() {
        return requesterUsername;
    }

    public void setRequesterUsername(String requesterUsername) {
        this.requesterUsername = requesterUsername;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void accept() {
        isAccepted = true;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public User getInvitedUser() {
        return invitedUser;
    }

    public void setInvitedUser(User invitedUser) {
        this.invitedUser = invitedUser;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void blockInvite() {
        this.isBlocked = true;
    }
}

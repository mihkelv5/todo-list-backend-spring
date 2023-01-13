package com.todolist.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "event_invitation")
public class EventInvitationModel implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String requesterUsername;
    @Column(nullable = false)
    private UUID eventId;

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
    private UserModel invitedUser;

    public EventInvitationModel() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 14);
        this.expirationDate = calendar.getTime();
        this.isAccepted = false;
        this.isBlocked = false;
    }

    public EventInvitationModel(UUID id, String requesterUsername, UUID eventId, boolean isAccepted, boolean isRejected, Date expirationDate, UserModel invitedUser) {
        this.id = id;
        this.requesterUsername = requesterUsername;
        this.eventId = eventId;
        this.isAccepted = isAccepted;
        this.isBlocked = isRejected;
        this.expirationDate = expirationDate;
        this.invitedUser = invitedUser;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRequesterUsername() {
        return requesterUsername;
    }

    public void setRequesterUsername(String requesterUsername) {
        this.requesterUsername = requesterUsername;
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
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

    public UserModel getInvitedUser() {
        return invitedUser;
    }

    public void setInvitedUser(UserModel invitedUser) {
        this.invitedUser = invitedUser;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void blockInvite() {
        this.isBlocked = true;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}

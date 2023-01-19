package com.todolist.entity.dto;

import com.todolist.entity.EventModel;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class EventModelDTO {
    private UUID id;
    private String title;
    private String description;
    private Set<PublicUserDTO> eventUsers;

    public EventModelDTO() {
    }

    public static EventModelDTO EventModelDTOConverter(EventModel eventModel){
        EventModelDTO event = new EventModelDTO();
        event.setId(eventModel.getId());
        event.setTitle(eventModel.getTitle());
        event.setDescription(eventModel.getDescription());

        Set<PublicUserDTO> eventUsers = eventModel.getEventUsers().stream().map(PublicUserDTO::publicUserDTOConverter).collect(Collectors.toSet());
        event.setEventUsers(eventUsers);
        return event;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<PublicUserDTO> getEventUsers() {
        return eventUsers;
    }

    public void setEventUsers(Set<PublicUserDTO> eventUsers) {
        this.eventUsers = eventUsers;
    }
}

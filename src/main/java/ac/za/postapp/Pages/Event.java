package ac.za.postapp.Pages;

import java.time.LocalDateTime;

public class Event {
    private int eventId;
    private String title;
    private String description;
    private String building;
    private String room;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String accessibility;
    private LocalDateTime createdAt;

    public Event() {}

    public Event(int eventId, String title, String description, String building,
                 String room, LocalDateTime startTime, LocalDateTime endTime,
                 String accessibility) {
        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.building = building;
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;
        this.accessibility = accessibility;
        this.createdAt = LocalDateTime.now();
    }

    public Event(int eventId, String title, String description, String building,
                 String room, String startTime, String endTime, String accessibility) {
        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.building = building;
        this.room = room;
        this.startTime = LocalDateTime.parse(startTime.replace(" ", "T"));
        this.endTime = LocalDateTime.parse(endTime.replace(" ", "T"));
        this.accessibility = accessibility;
        this.createdAt = LocalDateTime.now();
    }

    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getBuilding() { return building; }
    public void setBuilding(String building) { this.building = building; }

    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public String getAccessibility() { return accessibility; }
    public void setAccessibility(String accessibility) { this.accessibility = accessibility; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // Helper methods for string representations
    public String getStartTimeString() {
        return startTime.toString().replace("T", " ");
    }

    public String getEndTimeString() {
        return endTime.toString().replace("T", " ");
    }
}

package ac.za.postapp.Pages;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event {
    private int eventId;
    private String title;
    private String description;
    private String building;
    private String room;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String accessibility;

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
    }

    // String-based constructor for compatibility
    public Event(int eventId, String title, String description, String building,
                 String room, String startTime, String endTime, String accessibility) {
        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.building = building;
        this.room = room;
        this.startTime = parseDateTime(startTime);
        this.endTime = parseDateTime(endTime);
        this.accessibility = accessibility;
    }

    private LocalDateTime parseDateTime(String dateTimeStr) {
        try {
            // Handle both "yyyy-MM-dd HH:mm:ss" and "yyyy-MM-dd'T'HH:mm:ss" formats
            String formatted = dateTimeStr.replace(" ", "T");
            return LocalDateTime.parse(formatted);
        } catch (Exception e) {
            System.err.println("Error parsing date: " + dateTimeStr);
            return LocalDateTime.now();
        }
    }

    // Getters and Setters
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

    // Helper methods for string representations
    public String getStartTimeString() {
        return startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getEndTimeString() {
        return endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventId=" + eventId +
                ", title='" + title + '\'' +
                ", building='" + building + '\'' +
                ", room='" + room + '\'' +
                ", startTime=" + startTime +
                ", accessibility='" + accessibility + '\'' +
                '}';
    }
}
package ac.za.postapp.Pages;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class EventDAO {

    public static boolean save(Event event) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DatabaseConnection.getConnection();
            String sql = "INSERT INTO events (title, description, building, room, start_time, end_time, accessibility) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, event.getTitle());
            pstmt.setString(2, event.getDescription());
            pstmt.setString(3, event.getBuilding());
            pstmt.setString(4, event.getRoom());
            pstmt.setTimestamp(5, Timestamp.valueOf(event.getStartTime()));
            pstmt.setTimestamp(6, Timestamp.valueOf(event.getEndTime()));
            pstmt.setString(7, event.getAccessibility());

            int ok = pstmt.executeUpdate();
            if (ok > 0) {
                JOptionPane.showMessageDialog(null, "Event saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                return true;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error saving event: " + e.getMessage(),
                    "SQL Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Error closing database resources: " + e.getMessage());
            }
        }
        return false;
    }

    public static ArrayList<Event> getAll() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<Event> events = new ArrayList<>();

        try {
            con = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM events ORDER BY start_time";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Event event = new Event(
                        rs.getInt("event_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("building"),
                        rs.getString("room"),
                        rs.getTimestamp("start_time").toLocalDateTime(),
                        rs.getTimestamp("end_time").toLocalDateTime(),
                        rs.getString("accessibility")
                );
                events.add(event);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading events: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Error closing database resources: " + e.getMessage());
            }
        }
        return events;
    }

    public static ArrayList<Event> filter(String building, String date, String accessibility) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<Event> events = new ArrayList<>();

        try {
            con = DatabaseConnection.getConnection();

            String sql = "SELECT * FROM events WHERE building = ? AND DATE(start_time) = ? " +
                    "AND accessibility = ? ORDER BY start_time";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, building);
            pstmt.setDate(2, Date.valueOf(date));
            pstmt.setString(3, accessibility);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Event event = new Event(
                        rs.getInt("event_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("building"),
                        rs.getString("room"),
                        rs.getTimestamp("start_time").toLocalDateTime(),
                        rs.getTimestamp("end_time").toLocalDateTime(),
                        rs.getString("accessibility")
                );
                events.add(event);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Error closing database resources: " + e.getMessage());
            }
        }
        return events;
    }

    // Get events for a specific building
    public static ArrayList<Event> getEventsByBuilding(String building) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<Event> events = new ArrayList<>();

        try {
            con = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM events WHERE building = ? ORDER BY start_time";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, building);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Event event = new Event(
                        rs.getInt("event_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("building"),
                        rs.getString("room"),
                        rs.getTimestamp("start_time").toLocalDateTime(),
                        rs.getTimestamp("end_time").toLocalDateTime(),
                        rs.getString("accessibility")
                );
                events.add(event);
            }
        } catch (Exception e) {
            System.err.println("Error getting events by building: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Error closing database resources: " + e.getMessage());
            }
        }
        return events;
    }
}
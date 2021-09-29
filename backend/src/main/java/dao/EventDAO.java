package dao;

import model.Event;

import java.sql.*;
import java.util.ArrayList;

public class EventDAO {
    private final Connection conn;

    public EventDAO(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * inserts a new event into the database
     * @param event
     * @throws DataAccessException
     */
    public void insert(Event event) throws DataAccessException {
        String sql = "INSERT INTO Events (event_id, username, person_id, latitude, longitude, " +
                "country, city, event_type, year) VALUES(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getUsername());
            stmt.setString(3, event.getPersonID());
            stmt.setFloat(4, event.getLatitude());
            stmt.setFloat(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Given an eventID, returns a specific Event class
     * @param eventID
     * @return Event
     * @throws DataAccessException
     */
    public Event find(String eventID) throws DataAccessException {
        Event event;
        ResultSet rs = null;
        String sql = "SELECT * FROM events WHERE event_id = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event(rs.getString("event_id"), rs.getString("username"),
                        rs.getString("person_id"), rs.getFloat("latitude"), rs.getFloat("longitude"),
                        rs.getString("country"), rs.getString("city"), rs.getString("event_type"),
                        rs.getInt("year"));
                return event;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while finding event");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    /**
     * Builds an array list of all Events that are associated with a given username
     * @param username
     * @return
     * @throws DataAccessException
     */
    public ArrayList<Event> getAll(String username) throws DataAccessException{
        ArrayList<Event> events = new ArrayList<>();
        ResultSet rs = null;
        String sql = "SELECT * FROM events WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            while(rs.next()){
                Event event = new Event(rs.getString("event_id"), rs.getString("username"),
                        rs.getString("person_id"), rs.getFloat("latitude"),
                        rs.getFloat("longitude"), rs.getString("country"),
                        rs.getString("city"), rs.getString("event_type"), rs.getInt("year"));
                events.add(event);
            }
            return events;
        }
        catch (SQLException e){
            throw new DataAccessException();
        }
        finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * clears all Events from the database
     * @throws DataAccessException
     */
    public void clear() throws DataAccessException{
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM events";
            stmt.executeUpdate(sql);
        }
        catch(SQLException e){
            throw new DataAccessException("Error: could not clear events table");
        }

    }

    /**
     * Clears all Events tied to a given username from the database
     * @param username
     * @throws DataAccessException
     */
    public void clear(String username) throws DataAccessException{
        String sql = "DELETE FROM events WHERE username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, username);
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException("Error, could not clear events table for user " + username);
        }
    }
}
import dao.*;
import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class EventDAOTest {
    private Database db = new Database();
    private User user;
    private Person person;
    private Event event;
    private String username = "joshbt";
    private String password = "asdwer56wae";
    private String email = "joshbt@gmail.com";
    private String firstName = "josh";
    private String lastName = "taylor";
    private String gender = "m";
    private String personID = "51awe5r56a5w";
    private String fatherID = "dad";
    private String motherID = "mom";
    private String spouseID = "wife";
    private String eventID = "bwe51awe";
    private String country = "USA";
    private String city = "Provo";
    private String eventType = "graduation";
    private int year = 1555;
    private float latitude = (float) 89.124;
    private float longitude = (float) 13.12;
    private UserDAO userDAO;
    private PersonDAO personDAO;
    private EventDAO eventDAO;

    @BeforeEach
    public void setUp() throws DataAccessException{
        db = new Database();
        db.openConnection();
        userDAO = new UserDAO(db.getConnection());
        personDAO = new PersonDAO(db.getConnection());
        eventDAO = new EventDAO(db.getConnection());
        user = new User(username,password,email,firstName,lastName,gender,personID);
        person = new Person(personID,username,firstName,lastName,gender,fatherID,motherID,spouseID);
        event = new Event(eventID,username,personID,latitude,longitude,country,city,eventType,year);
    }

    @AfterEach
    public void cleanUp() throws DataAccessException{
        db.clearTables();
        db.closeConnection(false);
    }

    @Test
    public void testInsert(){
        try{
            userDAO.insert(user);
            personDAO.insert(person);
            eventDAO.insert(event);
            assertEquals(eventType, eventDAO.find(eventID).getEventType());
            assertEquals(eventID, eventDAO.find(eventID).getEventID());
            assertEquals(username, eventDAO.find(eventID).getUsername());
        } catch (Exception exception) {
            fail(exception.getMessage());
        }
    }

    @Test
    public void testInsertError(){
        try{
            userDAO.insert(user);
            personDAO.insert(person);
            eventDAO.insert(event);
            assertThrows(DataAccessException.class, ()->{eventDAO.insert(event);});
            event.setEventID(null);
            assertThrows(DataAccessException.class, ()->{eventDAO.insert(event);});
            event.setEventID(eventID);
            eventDAO.clear();
            eventDAO.insert(event);
        } catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testFind(){
        try{
            userDAO.insert(user);
            personDAO.insert(person);
            eventDAO.insert(event);
            Event result = eventDAO.find(eventID);
            assertEquals(personID, result.getPersonID());
            assertEquals(eventID, result.getEventID());
            assertEquals(longitude, result.getLongitude());
            assertEquals(country, result.getCountry());
            assertEquals(city, result.getCity());
            assertEquals(year, result.getYear());
        }
        catch (DataAccessException e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testFindError(){
        try{
            userDAO.insert(user);
            personDAO.insert(person);
            eventDAO.insert(event);
            event.setEventID(eventID + "2");
            eventDAO.insert(event);
            Event result = eventDAO.find(eventID + "2");
            assertEquals(personID, result.getPersonID());
            assertNotEquals(eventID, result.getEventID());
            assertEquals(longitude, result.getLongitude());
            result = eventDAO.find("notanevent");
            assertNull(result);
        }
        catch (DataAccessException e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testClearTwo(){
        try{
            userDAO.insert(user);
            personDAO.insert(person);
            eventDAO.insert(event);
            event.setEventID("event 2");
            eventDAO.insert(event);
            assertEquals(2, eventDAO.getAll(username).size());
            eventDAO.clear();
            assertEquals(0, eventDAO.getAll(username).size());
            Event result = eventDAO.find(eventID);
            assertNull(result);
        }
        catch(DataAccessException e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetAll(){
        try{
            userDAO.insert(user);
            personDAO.insert(person);
            eventDAO.insert(event);
            event.setEventID("event2");
            event.setCountry("Mexico");
            eventDAO.insert(event);
            ArrayList<Event> result = eventDAO.getAll(username);
            assertEquals(eventID, result.get(0).getEventID());
            assertEquals(country, result.get(0).getCountry());
            assertEquals("event2", result.get(1).getEventID());
            assertEquals("Mexico", result.get(1).getCountry());

        }
        catch(DataAccessException e){
            fail(e.getMessage());
        }
    }
    @Test
    public void testGetAllError(){
        try{
            userDAO.insert(user);
            personDAO.insert(person);
            eventDAO.insert(event);
            event.setEventID("event2");
            event.setCountry("Mexico");
            event.setUsername("another user");
            eventDAO.insert(event);
            ArrayList<Event> result = eventDAO.getAll(username);
            assertEquals(1, result.size());
            assertEquals(eventID, result.get(0).getEventID());
            assertEquals(country, result.get(0).getCountry());
            result = eventDAO.getAll("another user");
            assertEquals(1, result.size());
            assertEquals("event2", result.get(0).getEventID());
            assertEquals("Mexico", result.get(0).getCountry());
        }
        catch(DataAccessException e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testClearUsername(){
        try{
            userDAO.insert(user);
            personDAO.insert(person);
            eventDAO.insert(event);
            event.setEventID("event2");
            event.setCountry("Mexico");
            event.setUsername("another user");
            eventDAO.insert(event);
            eventDAO.clear(username);
            ArrayList<Event> result = eventDAO.getAll(username);
            assertEquals(0, result.size());
            result = eventDAO.getAll("another user");
            assertEquals(1, result.size());
            assertEquals("event2", result.get(0).getEventID());
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testClearUsernameTwo(){
        try{
            userDAO.insert(user);
            personDAO.insert(person);
            eventDAO.insert(event);
            event.setEventID("event2");
            event.setCountry("Mexico");
            event.setUsername("another user");
            eventDAO.insert(event);
            event.setEventID("event3");
            event.setCountry("Canada");
            eventDAO.insert(event);
            eventDAO.clear("another user");
            ArrayList<Event> result = eventDAO.getAll("another user");
            assertEquals(0, result.size());
            result = eventDAO.getAll(username);
            assertEquals(1, result.size());
            eventDAO.clear(username);
            result = eventDAO.getAll(username);
            assertEquals(0, result.size());
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }





}

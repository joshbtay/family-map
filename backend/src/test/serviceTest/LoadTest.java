import Request.LoadRequest;
import Result.LoadResult;
import dao.*;
import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.LoadService;
import service.LoginService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class LoadTest {
    private LoginService loginService = new LoginService();
    private LoadService loadService = new LoadService();
    private Database db;
    //first user
    private String username1 = "joshbt";
    private String password1 = "cvz15fv5r";
    private String email1 = "joshtt24@gmail.com";
    private String firstName1 = "josh";
    private String lastName1 = "josh";
    private String gender1 = "m";
    private String personID1 = "e156we56sd5";
    private String fatherID1 = "kmekmicvie";
    private String motherID1 = "meiodvbio";
    private String spouseID1 = "moacveaor";
    private String country1 = "usa";
    private String city1 = "Houston";
    private String eventType1 = "Birth";
    private String eventType2 = "Death";
    private int year1 = 1900;
    private float latitude1 = (float) 1.8;
    private float longitude1 = (float) 444.888;
    private String eventID1 = "5w1ev5xc";
    private String eventID3 = "kmrowegoi";
    private User user1;
    //second user
    private String username2 = "sarahm";
    private String password2 = "riimtcxvv5r";
    private String email2 = "sarah@gmail.com";
    private String firstName2 = "sarah";
    private String lastName2 = "m";
    private String gender2 = "f";
    private String personID2 = "wefdcver5";
    private String fatherID2 = "ujrnett";
    private String motherID2 = "wfvrttb";
    private String spouseID2 = "wrrtyhxvcg";
    private String city2 = "Seattle";
    private int year2 = 1855;
    private float latitude2 = (float) 524.23;
    private float longitude2 = (float) 1;
    private String eventID2 = "5w1ewevcewwexc";
    private User user2;

    private Person person1;
    private Person person2;
    private Event event1;
    private Event event2;
    private Event event3;
    private ArrayList<Event> events;
    private ArrayList<User> users;
    private ArrayList<Person> persons;


    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
        generateArrays();
    }

    private void generateArrays(){
        events = new ArrayList<>();
        users = new ArrayList<>();
        persons = new ArrayList<>();
        user1 = new User(username1, password1, email1, firstName1, lastName1, gender1, personID1);
        user2 = new User(username2, password2, email2, firstName2, lastName2, gender2, personID2);
        person1 = new Person(personID1, username1, firstName1, lastName1, gender1, fatherID1, motherID1, spouseID1);
        person2 = new Person(personID2, username2, firstName2, lastName2, gender2, fatherID2, motherID2, spouseID2);
        Person person3 = new Person(fatherID1, username1, lastName1, lastName1, gender1, null, null, motherID1);
        Person person4 = new Person(motherID1, username1, lastName1, lastName1, gender2, null, null, fatherID1);
        event1 = new Event(eventID1, username1, personID1, latitude1, longitude1, country1, city1, eventType1, year1);
        event2 = new Event(eventID2, username2, personID2, latitude2, longitude2, country1, city2, eventType1, year2);
        event3 = new Event(eventID3, username2, personID2, latitude2, longitude2, country1, city2, eventType2, year1);
        events.add(event1);
        events.add(event2);
        events.add(event3);
        users.add(user1);
        users.add(user2);
        persons.add(person1);
        persons.add(person2);
        persons.add(person3);
        persons.add(person4);
    }

    @Test
    public void testLoad(){
        try{
            loadService = new LoadService();
            LoadRequest request = new LoadRequest(users.toArray(new User[users.size()]),
                    persons.toArray(new Person[persons.size()]), events.toArray(new Event[events.size()]));
            LoadResult result = loadService.load(request);
            db = new Database();
            db.openConnection();
            UserDAO userDAO = new UserDAO(db.getConnection());
            User user = userDAO.find(username2);
            assertEquals(personID1, userDAO.find(username1).getPersonID());
            assertEquals(personID2, userDAO.find(username2).getPersonID());
            db.closeConnection(false);
        }
        catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testLoadError(){
        try{
            LoadRequest request = new LoadRequest(users.toArray(new User[users.size()]),
                    persons.toArray(new Person[persons.size()]), null);

            db = new Database();
            db.openConnection();
            EventDAO eventDAO = new EventDAO(db.getConnection());
            assertNull(eventDAO.find(eventID3));
            assertNull(eventDAO.find(eventID2));
            assertNull(eventDAO.find(eventID1));
            UserDAO userDAO = new UserDAO(db.getConnection());
            assertNull(userDAO.find(username2));
            PersonDAO personDAO = new PersonDAO(db.getConnection());
            assertEquals(0, personDAO.getAll(username1).size());
            db.closeConnection(false);

        }
        catch (Exception e) {
            fail(e.getMessage());
        }
    }

}

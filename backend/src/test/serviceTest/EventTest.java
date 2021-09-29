import Request.EventRequest;
import Request.RegisterRequest;
import Result.EventResult;
import Result.RegisterResult;
import dao.DataAccessException;
import dao.Database;
import dao.EventDAO;
import model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.EventService;
import service.RegisterService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class EventTest {
    private RegisterService registerService = new RegisterService();
    private EventService eventService = new EventService();
    private String username = "josh";
    private String password = "a5we51r65df1a5";
    private String email = "joshtt24@gmail.com";
    private String firstName = "josh";
    private String lastName = "taylor";
    private String gender = "m";
    private String personID = "We15s6d1awW";
    private Database db = new Database();
    private EventDAO eventDAO;
    private String eventID1;
    private String eventID2;
    private String eventID3;
    private String authToken;

    public void EventTest() throws DataAccessException {

    }

    @BeforeEach
    public void setUp() throws DataAccessException {
    }

    @Test
    public void testMultipleEvents() {
        try {
            db.openConnection();
            db.clearTables();
            db.closeConnection(true);
            RegisterResult regResult = registerService.register( new RegisterRequest(username, password, email,
                    firstName, lastName, gender));
            authToken = regResult.getAuthtoken();
            EventResult result = eventService.event(new EventRequest(authToken));
            ArrayList<Event> arrayList = result.getResults();
            assertNotNull(arrayList);
            assertEquals(91, result.getResults().size());
            eventID1 = arrayList.get(38).getEventID();
            eventID2 = arrayList.get(30).getEventID();
            eventID3 = arrayList.get(0).getEventID();
            result = eventService.event(new EventRequest(eventID1, authToken));
            assertEquals(eventID1, result.getEventID());
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testSingleEvent() {
        try {
            EventResult result = eventService.event(new EventRequest(eventID2, authToken));
            assertNull(result.getResults());
            assertEquals(eventID2, result.getEventID());
            result = eventService.event(new EventRequest(eventID3, authToken));
            assertNull(result.getResults());
            assertEquals(eventID3, result.getEventID());
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }


}
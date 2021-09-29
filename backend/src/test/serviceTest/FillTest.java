import Request.FillRequest;
import dao.*;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FillService;

import static org.junit.jupiter.api.Assertions.*;


public class FillTest {
    private FillService fillService = new FillService();
    private String username = "josh";
    private String password = "a5we51r65df1a5";
    private String email = "joshtt24@gmail.com";
    private String firstName = "josh";
    private String lastName = "taylor";
    private String gender = "m";
    private String personID = "We15s6d1awW";
    private Database db = new Database();
    private UserDAO userDAO;
    private PersonDAO personDAO;
    private EventDAO eventDAO;
    private User user;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();
        db.openConnection();
        userDAO = new UserDAO(db.getConnection());
        user = new User(username, password, email, firstName, lastName, gender, personID);
        userDAO.clear();
        userDAO.insert(user);
        personDAO = new PersonDAO(db.getConnection());
        personDAO.clear();
        eventDAO = new EventDAO(db.getConnection());
        eventDAO.clear();
        db.closeConnection(true);
    }

    @AfterEach
    public void cleanUp() throws DataAccessException {
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void testFill() {
        try {
            db.openConnection();
            personDAO = new PersonDAO(db.getConnection());
            assertEquals(0, personDAO.getAll(username).size());
            db.closeConnection(false);
            fillService.fill(new FillRequest(username, 3));
            db.openConnection();
            personDAO = new PersonDAO(db.getConnection());
            eventDAO = new EventDAO(db.getConnection());
            assertEquals(15, personDAO.getAll(username).size());
            assertEquals(43,eventDAO.getAll(username).size());
            assertEquals(user.getFirstName(), personDAO.getAll(username).get(0).getFirstName());
            db.closeConnection(false);
        }
        catch (Exception e) {
            fail(e.getMessage());
        }
    }


    @Test
    public void testFillNone() {
        try {
            db.openConnection();
            personDAO = new PersonDAO(db.getConnection());
            assertEquals(0, personDAO.getAll(username).size());
            db.closeConnection(false);
            fillService.fill(new FillRequest(username, 0));
            db.openConnection();
            personDAO = new PersonDAO(db.getConnection());
            eventDAO = new EventDAO(db.getConnection());
            assertEquals(1, personDAO.getAll(username).size());
            assertEquals(1,eventDAO.getAll(username).size());
            assertEquals(user.getFirstName(), personDAO.getAll(username).get(0).getFirstName());
            db.closeConnection(false);
        }
        catch (Exception e) {
            fail(e.getMessage());
        }

    }
}

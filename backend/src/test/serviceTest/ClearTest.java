import Request.ClearRequest;
import Request.FillRequest;
import dao.DataAccessException;
import dao.Database;
import dao.PersonDAO;
import dao.UserDAO;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ClearService;
import service.FillService;

import static org.junit.jupiter.api.Assertions.*;

public class ClearTest {
    private ClearService clearService = new ClearService();
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
        db.closeConnection(true);
    }

    @AfterEach
    public void cleanUp() throws DataAccessException {
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }


    @Test
    public void testClear() {
        try {
            db.openConnection();
            personDAO = new PersonDAO(db.getConnection());
            assertEquals(0, personDAO.getAll(username).size());
            db.closeConnection(false);
            fillService.fill(new FillRequest(username, 0));

            clearService.clear(new ClearRequest());

            db.openConnection();
            personDAO = new PersonDAO(db.getConnection());
            assertEquals(0, personDAO.getAll(username).size());
            assertNull(personDAO.find(personID));
            db.closeConnection(false);
        }
        catch (Exception e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void testClearNone() {
        try {
            db.openConnection();
            personDAO = new PersonDAO(db.getConnection());
            userDAO = new UserDAO(db.getConnection());
            assertEquals(0, personDAO.getAll(username).size());
            db.clearTables();
            assertNull(userDAO.find(username));
            db.closeConnection(false);

            clearService.clear(new ClearRequest());

            db.openConnection();
            personDAO = new PersonDAO(db.getConnection());
            userDAO = new UserDAO(db.getConnection());
            assertNull(userDAO.find(username));
            assertEquals(0, personDAO.getAll(username).size());
            assertNull(personDAO.find(personID));
            db.closeConnection(false);
        }
        catch (Exception e) {
            fail(e.getMessage());
        }

    }
}

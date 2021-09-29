import Request.RegisterRequest;
import dao.*;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.RegisterService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class RegisterTest {
    private RegisterService registerService = new RegisterService();
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
    public void setUp() throws DataAccessException{
        db = new Database();
        db.openConnection();
        db.clearTables();
        userDAO = new UserDAO(db.getConnection());
        user = new User(username, password, email, firstName, lastName, gender, personID);
        db.closeConnection(true);
    }

    @AfterEach
    public void cleanUp() throws DataAccessException {
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void testRegister(){
        try{
            registerService.register(new RegisterRequest(username, password, email,
                    firstName, lastName, gender));
            db.openConnection();
            personDAO = new PersonDAO(db.getConnection());
            userDAO = new UserDAO(db.getConnection());
            eventDAO = new EventDAO(db.getConnection());
            //check that it generated the proper amount of users, events, and people
            assertEquals(email, userDAO.find(username).getEmail());
            assertEquals(31, personDAO.getAll(username).size());
            assertEquals(91, eventDAO.getAll(username).size());
            db.closeConnection(false);
        }
        catch (Exception e) {
            fail(e.getMessage());
        }
    }
    @Test
    public void testRegisterTwo(){
        try{
            registerService.register(new RegisterRequest(username, password, email,
                    firstName, lastName, gender));
            registerService.register(new RegisterRequest(username + "2", password, email + "2",
                    firstName, lastName, gender));
            db.openConnection();
            personDAO = new PersonDAO(db.getConnection());
            userDAO = new UserDAO(db.getConnection());
            eventDAO = new EventDAO(db.getConnection());
            //check that it generated the proper amount of users, events, and people
            assertEquals(email, userDAO.find(username).getEmail());
            assertEquals(31, personDAO.getAll(username).size());
            assertEquals(91, eventDAO.getAll(username).size());
            assertEquals(username, eventDAO.getAll(username).get(20).getUsername());
            assertEquals(username + "2", eventDAO.getAll(username + "2").get(45).getUsername());
            db.closeConnection(false);
        }
        catch (Exception e) {
            fail(e.getMessage());
        }


    }

}

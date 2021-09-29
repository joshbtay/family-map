import Request.LoginRequest;
import Result.LoginResult;
import dao.DataAccessException;
import dao.Database;
import dao.UserDAO;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.LoginService;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {
    private String username = "josh";
    private String password = "a5we51r65df1a5";
    private String email = "joshtt24@gmail.com";
    private String firstName = "josh";
    private String lastName = "taylor";
    private String gender = "m";
    private String personID = "We15s6d1awW";
    private User user;
    private Database db = new Database();
    @BeforeEach
    public void setUp() throws DataAccessException {
        user = new User(username,password,email,firstName,lastName,gender,personID);
        db = new Database();
        db.openConnection();
        db.clearTables();
        UserDAO userDAO = new UserDAO(db.getConnection());
        userDAO.insert(user);
        db.closeConnection(true);
    }

    @AfterEach
    public void cleanUp() throws DataAccessException {
        db = new Database();
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void testLogin(){
        try {
            LoginService loginService = new LoginService();
            LoginResult loginResult = loginService.login(new LoginRequest(username, password));
            assertEquals(username, loginResult.getUsername());
            assertEquals(personID, loginResult.getPersonID());
            assertNotNull(loginResult.getAuthtoken());
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void BadLogin(){
        try {
            LoginService loginService = new LoginService();
            LoginResult loginResult = loginService.login(new LoginRequest(username + "2", password));
            assertFalse(loginResult.getSuccess());
            assertNull(loginResult.getAuthtoken());
            loginResult = loginService.login(new LoginRequest(username, password + '2'));
            assertFalse(loginResult.getSuccess());
            assertNull(loginResult.getAuthtoken());
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }




}

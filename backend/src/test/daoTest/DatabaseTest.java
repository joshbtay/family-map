import dao.DataAccessException;
import dao.Database;
import dao.UserDAO;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class DatabaseTest {

    private Database db;
    private UserDAO userDao;
    private String username = "joshbt";
    private String password = "asdwer56wae";
    private String email = "joshbt@gmail.com";
    private String firstName = "josh";
    private String lastName = "taylor";
    private String gender = "m";
    private String personID = "51awe5r56a5w";
    private User test = new User(username,password,email,firstName,lastName,gender,personID);

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();
        db.openConnection();
        userDao = new UserDAO(db.getConnection());
        userDao.clear();
    }

    @AfterEach
    public void CleanUp() throws DataAccessException{
        db.closeConnection(false);
    }

    @Test
    public void testClearTables(){

        try {
            userDao.insert(test);
            assertEquals(personID, userDao.find(username).getPersonID());
            db.clearTables();
            assertNull(userDao.find(username));
        }
        catch (Exception e){
            fail(e.getMessage());
        }

    }
}

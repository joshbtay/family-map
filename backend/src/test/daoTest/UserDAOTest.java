import dao.DataAccessException;
import dao.Database;
import dao.UserDAO;
import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {
    private Database db = new Database();
    private User user;
    String username = "josh";
    String password = "m51weasdf5we5";
    String email = "joshtt24@gmail.com";
    String firstName = "josh";
    String lastName = "taylor";
    String gender = "m";
    String personID = "va5efas51w";
    UserDAO userDAO;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();
        db.openConnection();
        userDAO = new UserDAO(db.getConnection());
        userDAO.clear();
        user = new User(username,password,email,firstName,lastName,gender,personID);

    }

    @AfterEach
    public void cleanUp() throws DataAccessException {
        userDAO.clear();
        db.closeConnection(false);
    }

    //positive test insert
    @Test
    public void testInsert(){
        try{
            assertDoesNotThrow(()->{userDAO.insert(user);});

        }
        catch(Exception e){
            fail(e.getMessage());
        }
    }

    //negative test insert
    @Test
    public void testInsertError(){
        try{

            userDAO.insert(user);
            //attempting to insert same user a second time should fail
            assertThrows(DataAccessException.class, ()->{userDAO.insert(user);});
            user.setUsername(null);
            assertThrows(DataAccessException.class, ()->{userDAO.insert(user);});
        }
        catch(Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testFind(){
        try{
            userDAO.insert(user);

            User result = userDAO.find(username);
            assertEquals(username, result.getUsername());
            assertEquals(email, result.getEmail());
            assertEquals(gender, result.getGender());
            assertEquals(password, result.getPassword());
            assertEquals(firstName, result.getFirstName());
            assertEquals(lastName, result.getLastName());
            assertEquals(personID, result.getPersonID());
        } catch (DataAccessException e) {
            fail(e.getMessage());
        }
    }

    public String edit(String input){
        StringBuilder edit = new StringBuilder(input);
        edit.append("2");
        return edit.toString();
    }

    public void insertSecond() throws DataAccessException {
        User user2 = new User(edit(username), edit(password), edit(email), edit(firstName), edit(lastName), gender, edit(personID));
        userDAO.insert(user2);
    }

    @Test
    public void testFindError(){
        try{
            insertSecond();
            userDAO.insert(user);
            User result = userDAO.find(edit(username));
            assertEquals(edit(username), result.getUsername(), username);
            assertEquals(edit(email), result.getEmail());
            assertEquals(edit(password), result.getPassword());
            assertEquals(edit(firstName), result.getFirstName());
            result = userDAO.find("not a user");
            assertNull(result);
        } catch (DataAccessException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testClear(){
        try{
            userDAO.insert(user);
            insertSecond();
            User result = userDAO.find(username);
            assertEquals(result.getPersonID(), personID);
            userDAO.clear();
            result = userDAO.find(username);
            assertNull(result);

        }
        catch(DataAccessException e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testClearTwo() {
        try {
            userDAO.insert(user);
            insertSecond();
            userDAO.clear();
            User result = userDAO.find(username);
            assertNull(result);
            result = userDAO.find(edit(username));
            assertNull(result);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }




}

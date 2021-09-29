import dao.*;
import model.AuthToken;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthTokenDAOTest {
    private Database db = new Database();
    private AuthTokenDAO authTokenDAO;
    private String id1 = "51w6ae561";
    private String id2 = "waer1565w";
    private String id3 = "aw1e5r651";
    private String username = "josh";
    private String username2 = "user 2";
    private AuthToken authToken1 = new AuthToken(id1, username);
    private AuthToken authToken2 = new AuthToken(id2, username);
    private AuthToken authToken3 = new AuthToken(id3, username2);

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();
        db.openConnection();
        authTokenDAO = new AuthTokenDAO(db.getConnection());
    }

    @AfterEach
    public void cleanUp() throws DataAccessException{
        db.closeConnection(false);
    }

    @Test
    public void testInsert(){
        try{
            assertNull(authTokenDAO.find(username));
            authTokenDAO.insert(authToken1);
            authTokenDAO.insert(authToken2);
            assertEquals(username, authTokenDAO.find(id1).getUsername());
            assertEquals(username, authTokenDAO.find(id2).getUsername());
            assertNull(authTokenDAO.find(username2));
        }
        catch(Exception e){
            fail(e.getMessage());
        }
    }
    @Test
    public void testInsertTwo(){
        try{
            assertNull(authTokenDAO.find(username));
            authTokenDAO.insert(authToken1);
            authTokenDAO.insert(authToken2);
            authTokenDAO.insert(authToken3);
            assertEquals(username, authTokenDAO.find(id1).getUsername());
            assertEquals(username, authTokenDAO.find(id2).getUsername());
            assertThrows(DataAccessException.class, ()->{authTokenDAO.insert(authToken1);});
        }
        catch(Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testFind(){
        try{
            assertNull(authTokenDAO.find(username));
            authTokenDAO.insert(authToken1);
            authTokenDAO.insert(authToken2);
            assertEquals(username, authTokenDAO.find(id1).getUsername());
            assertEquals(id1, authTokenDAO.find(id1).getAuth_token());
            assertEquals(username, authTokenDAO.find(id2).getUsername());
            assertEquals(id2, authTokenDAO.find(id2).getAuth_token());
        }
        catch(Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testFindError(){
        try{
            assertNull(authTokenDAO.find(username));
            authTokenDAO.insert(authToken1);
            authTokenDAO.insert(authToken2);
            assertEquals(username, authTokenDAO.find(id1).getUsername());
            assertEquals(id1, authTokenDAO.find(id1).getAuth_token());
            assertNull(authTokenDAO.find(id3));
            assertEquals(id2, authTokenDAO.find(id2).getAuth_token());
        }
        catch(Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testClear(){
        try{
            assertNull(authTokenDAO.find(username));
            authTokenDAO.insert(authToken1);
            assertEquals(username, authTokenDAO.find(id1).getUsername());
            assertNull(authTokenDAO.find(id2));
            authTokenDAO.clear();
            assertNull(authTokenDAO.find(id1));
            assertNull(authTokenDAO.find(id2));
        }
        catch(Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testClearTwo(){
        try{
            assertNull(authTokenDAO.find(username));
            authTokenDAO.insert(authToken1);
            authTokenDAO.insert(authToken2);
            assertEquals(username, authTokenDAO.find(id1).getUsername());
            assertEquals(id2, authTokenDAO.find(id2).getAuth_token());
            authTokenDAO.clear();
            assertNull(authTokenDAO.find(id1));
            assertNull(authTokenDAO.find(id2));
        }
        catch(Exception e){
            fail(e.getMessage());
        }
    }
}

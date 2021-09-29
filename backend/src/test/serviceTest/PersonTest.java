import Request.PersonRequest;
import Request.RegisterRequest;
import Result.PersonResult;
import Result.RegisterResult;
import dao.DataAccessException;
import dao.Database;
import model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.PersonService;
import service.RegisterService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PersonTest {
    private RegisterService registerService = new RegisterService();
    private PersonService personService = new PersonService();
    private String username = "josh";
    private String password = "a5we51r65df1a5";
    private String email = "joshtt24@gmail.com";
    private String firstName = "josh";
    private String lastName = "taylor";
    private String gender = "m";
    private String personID = "We15s6d1awW";
    private Database db = new Database();
    private String personID1;
    private String personID2;
    private String personID3;
    private String authToken;

    @BeforeEach
    public void setUp() throws DataAccessException {
    }

    @Test
    public void testMultiplePersons() {
        try {
            db.openConnection();
            db.clearTables();
            db.closeConnection(true);
            RegisterResult regResult = registerService.register( new RegisterRequest(username, password, email,
                    firstName, lastName, gender));
            authToken = regResult.getAuthtoken();
            PersonResult result = personService.person(new PersonRequest(authToken));
            ArrayList<Person> arrayList = result.getPersons();
            assertNotNull(arrayList);
            assertEquals(31, result.getPersons().size());
            personID1 = arrayList.get(25).getPersonID();
            personID2 = arrayList.get(30).getPersonID();
            personID3 = arrayList.get(0).getPersonID();
            result = personService.person(new PersonRequest(personID1, authToken));
            assertEquals(personID1, result.getPersonID());
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testSinglePerson() {
        try {
            PersonResult result = personService.person(new PersonRequest(personID2, authToken));
            assertNull(result.getPersons());
            assertEquals(personID2, result.getPersonID());
            result = personService.person(new PersonRequest(personID3, authToken));
            assertNull(result.getPersons());
            assertEquals(personID3, result.getPersonID());
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }


}
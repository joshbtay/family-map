import dao.DataAccessException;
import dao.Database;
import dao.PersonDAO;
import dao.UserDAO;
import model.Event;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class PersonDAOTest {
    private Database db = new Database();
    private Person person;
    private User user;
    String username = "josh";
    String password = "m51weasdf5we5";
    String email = "joshtt24@gmail.com";
    String firstName = "josh";
    String lastName = "taylor";
    String gender = "m";
    String personID = "va5efas51w";
    String fatherID = "dad";
    String motherID = "mom";
    String spouseID = "wife";
    UserDAO userDAO;
    PersonDAO personDAO;

    @BeforeEach
    public void setUp() throws DataAccessException{

        db = new Database();
        db.openConnection();
        userDAO = new UserDAO(db.getConnection());
        personDAO = new PersonDAO(db.getConnection());
        user = new User(username,password,email,firstName,lastName,gender,personID);
        person = new Person(personID, username, firstName, lastName, gender, fatherID, motherID, spouseID);
    }

    @AfterEach
    public void cleanUp() throws DataAccessException{
        personDAO.clear();
        userDAO.clear();
        db.closeConnection(false);
    }

    @Test
    public void testInsert(){
        try{
            userDAO.insert(user);
            personDAO.insert(person);
            assertEquals(personID, personDAO.find(personID).getPersonID());
            assertEquals(username, personDAO.find(personID).getAssociatedUsername());
            assertEquals(firstName, personDAO.find(personID).getFirstName());
            assertEquals(lastName, personDAO.find(personID).getLastName());
        }
        catch(Exception e){
            fail(e.getMessage());
        }

    }

    @Test
    public void testInsertError(){
        try{
            userDAO.insert(user);
            personDAO.insert(person);
            assertThrows(DataAccessException.class, ()->{personDAO.insert(person);});
            Person person2 = person;
            person2.setPersonID(null);
            assertThrows(DataAccessException.class, ()->{personDAO.insert(person2);});
        }
        catch(Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testFind(){
        try{
            userDAO.insert(user);
            personDAO.insert(person);
            Person result = personDAO.find(personID);
            assertEquals(personID, result.getPersonID());
            assertEquals(username, result.getAssociatedUsername());
            assertEquals(firstName, result.getFirstName());
            assertEquals(lastName, result.getLastName());
            assertEquals(gender, result.getGender());
            assertEquals(fatherID, result.getFatherID());
            assertEquals(motherID, result.getMotherID());
            assertEquals(spouseID, result.getSpouseID());

        }
        catch (DataAccessException e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testFindError(){
        try{
            userDAO.insert(user);
            personDAO.insert(person);
            person.setPersonID(personID + "2");
            person.setFirstName(firstName + "2");
            personDAO.insert(person);
            Person result = personDAO.find(personID + "2");
            assertEquals(username, person.getAssociatedUsername());
            assertEquals(motherID, person.getMotherID());
            assertNotEquals(personID, person.getPersonID());
            assertNotEquals(firstName, person.getFirstName());
            result = personDAO.find(personID + "21");
            assertNull(result);

        } catch (DataAccessException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testClear(){
        try{
            userDAO.insert(user);
            personDAO.insert(person);
            Person result = personDAO.find(personID);
            assertEquals(fatherID, result.getFatherID());
            personDAO.clear();
            result = personDAO.find(personID);
            assertNull(result);
        }
        catch(DataAccessException e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testClearTwo(){
        try{
            userDAO.insert(user);
            personDAO.insert(person);
            person.setPersonID(personID + "2");
            person.setFirstName(firstName + "2");
            personDAO.insert(person);
            personDAO.clear();
            Person result = personDAO.find(personID);
            assertNull(result);
            result = personDAO.find(personID + "2");
            assertNull(result);
        }
        catch(DataAccessException e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetAll(){
        try{
            userDAO.insert(user);
            personDAO.insert(person);
            person.setPersonID(person.getPersonID() + "2");
            person.setFirstName(person.getFirstName() + "2");
            personDAO.insert(person);
            ArrayList<Person> result = personDAO.getAll(username);
            assertEquals(firstName, result.get(0).getFirstName());
            assertEquals(personID, result.get(0).getPersonID());
            assertEquals(personID + "2", result.get(1).getPersonID());
            assertEquals(firstName + "2", result.get(1).getFirstName());

        }
        catch(DataAccessException e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetAllError(){
        try{
            userDAO.insert(user);
            personDAO.insert(person);
            person.setPersonID(person.getPersonID() + "2");
            person.setFirstName(person.getFirstName() + "2");
            personDAO.insert(person);
            person.setPersonID(person.getPersonID() + "2");
            person.setFirstName(person.getFirstName() + "2");
            person.setAssociatedUsername("not person");
            personDAO.insert(person);
            ArrayList<Person> result = personDAO.getAll(username);
            assertEquals(2, result.size());
            assertEquals(personID, result.get(0).getPersonID());
            assertEquals(personID + "2", result.get(1).getPersonID());
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testClearUsername(){
        try{
            userDAO.insert(user);
            personDAO.insert(person);
            person.setPersonID(person.getPersonID() + "2");
            person.setFirstName(person.getFirstName() + "2");
            personDAO.insert(person);
            person.setPersonID(person.getPersonID() + "2");
            person.setFirstName(person.getFirstName() + "2");
            person.setAssociatedUsername("not person");
            personDAO.insert(person);
            assertEquals(username, personDAO.find(personID).getAssociatedUsername());
            assertEquals(username, personDAO.find(personID).getAssociatedUsername());
            personDAO.clear(username);
            ArrayList<Person> result = personDAO.getAll(username);
            assertEquals(0, result.size());
            assertThrows(IndexOutOfBoundsException.class, ()->{result.get(1);});
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testClearUsernameTwo(){
        try{
            userDAO.insert(user);
            personDAO.insert(person);
            person.setPersonID(person.getPersonID() + "2");
            person.setFirstName(person.getFirstName() + "2");
            personDAO.insert(person);
            person.setPersonID(person.getPersonID() + "2");
            person.setFirstName(person.getFirstName() + "2");
            person.setAssociatedUsername("not person");
            personDAO.insert(person);
            assertEquals(username, personDAO.find(personID).getAssociatedUsername());
            assertEquals(username, personDAO.find(personID).getAssociatedUsername());
            personDAO.clear(username);
            ArrayList<Person> result = personDAO.getAll("not person");
            assertEquals(1, result.size());
            assertThrows(IndexOutOfBoundsException.class, ()->{result.get(2);});
            assertEquals(personID + "22", result.get(0).getPersonID());
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }



}

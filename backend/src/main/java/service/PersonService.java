package service;

import Request.PersonRequest;
import Result.PersonResult;
import dao.*;
import model.AuthToken;
import model.Person;

public class PersonService {
    private PersonDAO personDAO;
    private AuthTokenDAO authTokenDAO;
    private Database db;

    private void openConnection() throws DataAccessException {
        db = new Database();
        db.openConnection();
        personDAO = new PersonDAO(db.getConnection());
        authTokenDAO = new AuthTokenDAO(db.getConnection());
    }

    /**
     * Makes a request to access a person or all persons and returns the result
     * @param personRequest
     * @return PersonResult
     */
    public PersonResult person(PersonRequest personRequest){
        try {
            openConnection();
            AuthToken token = authTokenDAO.find(personRequest.getAuthToken());
            if(token == null) {
                return new PersonResult(false, "Error: token is invalid");
            }
            if(personRequest.getPersonID() == null) {
                return new PersonResult(true, personDAO.getAll(token.getUsername()));
            }
            Person person = personDAO.find(personRequest.getPersonID());
            if(person == null) {
                return new PersonResult(false, "Error: no person found");
            }
            if(!person.getAssociatedUsername().equals(token.getUsername())) {
                return new PersonResult(false, "Error: person does not belong to user");
            }
            return new PersonResult(true, person);
        }
        catch (DataAccessException e) {
            return new PersonResult(false, "Error: problem while finding person");
        }
        finally {
            try {
                db.closeConnection(false);
            }
            catch (DataAccessException e){
                e.printStackTrace();
            }
        }
    }
}
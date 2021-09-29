package service;


import Result.LoadResult;
import dao.*;
import model.Event;
import model.Person;
import model.User;
import Request.LoadRequest;

public class LoadService {
    private UserDAO userDAO;
    private PersonDAO personDAO;
    private EventDAO eventDAO;
    private Database db;

    private void openConnection() throws DataAccessException {
        db = new Database();
        db.openConnection();
        userDAO = new UserDAO(db.getConnection());
        personDAO = new PersonDAO(db.getConnection());
        eventDAO = new EventDAO(db.getConnection());
    }

    /**
     * Makes a request to dump server's data and reload everything from the LoadRequest object
     * @param loadRequest
     * @return loadResult
     */
    public LoadResult load(LoadRequest loadRequest){
        try {
            openConnection();
            db.clearTables();
            for(User user : loadRequest.getUsers()){
                userDAO.insert(user);
            }
            for(Person person : loadRequest.getPersons()){
                personDAO.insert(person);
            }
            for(Event event : loadRequest.getEvents()){
                eventDAO.insert(event);
            }
            db.closeConnection(true);
            return new LoadResult(true, "Successfully added " +
                    loadRequest.getUsers().length + " users, " + loadRequest.getPersons().length +
                    " persons, and " + loadRequest.getEvents().length + " events to the database");

        } catch (DataAccessException e){
            try{
                db.closeConnection(false);
            } catch (DataAccessException exception){
                exception.printStackTrace();
            }
        }
        return new LoadResult(false, "Error: unable to load arrays");
    }
}

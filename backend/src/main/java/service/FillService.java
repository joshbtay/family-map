package service;

import RandomGenerator.AncestorData;
import RandomGenerator.RandomGenerations;
import Request.FillRequest;
import Result.FillResult;
import dao.*;
import model.Event;
import model.Person;
import model.User;

import java.sql.SQLException;
import java.util.Random;

public class FillService {
    private UserDAO userDAO;
    private PersonDAO personDAO;
    private EventDAO eventDAO;
    RandomGenerations rg = new RandomGenerations();
    private Database db;


    private void openConnection() throws DataAccessException {
        db = new Database();
        db.openConnection();
        userDAO = new UserDAO(db.getConnection());
        personDAO = new PersonDAO(db.getConnection());
        eventDAO = new EventDAO(db.getConnection());
    }

    /**
     * Makes a request to fill a person's data given a fillRequest object
     * @param fillRequest
     * @return fillResult
     */
    public FillResult fill(FillRequest fillRequest){
        try {
            openConnection();
            User user = userDAO.find(fillRequest.getUsername());
            if(user == null){
                db.closeConnection(false);
                return new FillResult(false, "Error: user not found");
            }
            personDAO.clear(user.getUsername());
            eventDAO.clear(user.getUsername());
            rg.generateAncestors(fillRequest.getGenerations(), user);
            AncestorData data = rg.getData();
            for( Person person : data.getPersons()){
                personDAO.insert(person);
            }
            for(Event event : data.getEvents()){
                eventDAO.insert(event);
            }
            db.closeConnection(true);
            int persons = (int)Math.pow(2, fillRequest.getGenerations() + 1) - 1;
            return new FillResult(true, "Successfully added " + persons +
                    " persons and " + (persons * 3 - 2) + " events to the database");
        }
        catch (DataAccessException e) {
            try{
                db.closeConnection(false);
            } catch (DataAccessException exception){
                exception.printStackTrace();
            }
        }
        return new FillResult(false, "Error, could not fill");
    }







}

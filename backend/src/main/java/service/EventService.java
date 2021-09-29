package service;

import Request.EventRequest;
import Result.EventResult;
import Result.PersonResult;
import dao.*;
import model.AuthToken;
import model.Event;

public class EventService {
    private EventDAO eventDAO;
    private AuthTokenDAO authTokenDAO;
    private Database db;

    private void openConnection() throws DataAccessException {
        db = new Database();
        db.openConnection();
        eventDAO = new EventDAO(db.getConnection());
        authTokenDAO = new AuthTokenDAO(db.getConnection());
    }

    /**
     * Given a request for an event or all events, attempts to access the result
     * @param eventRequest
     * @return EventResult
     */
    public EventResult event(EventRequest eventRequest){
        try {
            openConnection();
            AuthToken token = authTokenDAO.find(eventRequest.getAuthToken());
            if(token == null){
                return new EventResult(false, "Error: token is invalid");
            }
            if(eventRequest.getEventID() == null){
                return new EventResult(true, eventDAO.getAll(token.getUsername()));
            }
            Event event = eventDAO.find(eventRequest.getEventID());
            if(event == null){
                return new EventResult(false, "Error: no event found");
            }
            if(!event.getUsername().equals(token.getUsername())){
                return new EventResult(false, "Error: event does not belong to user");
            }
            return new EventResult(true, event);
        } catch (DataAccessException e) {
            return new EventResult(false, "Error: problem while finding event");
        } finally {
            try{
                db.closeConnection(false);
            } catch (DataAccessException e){
                e.printStackTrace();
            }
        }
    }
}

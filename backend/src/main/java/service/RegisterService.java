package service;
import RandomGenerator.RandomID;
import Request.FillRequest;
import Request.RegisterRequest;
import Result.RegisterResult;
import dao.*;
import model.AuthToken;
import model.User;

public class RegisterService {
    private UserDAO userDAO;
    private AuthTokenDAO authTokenDAO;
    private Database db = new Database();
    private RandomID randomID = new RandomID();

    private void openConnection() throws DataAccessException {
        db = new Database();
        db.openConnection();
        userDAO = new UserDAO(db.getConnection());
        authTokenDAO = new AuthTokenDAO(db.getConnection());
    }

    private User getUser(RegisterRequest request){
        User user = new User(request.getUsername(), request.getPassword(),
                request.getEmail(), request.getFirstName(), request.getLastName(),
                request.getGender(), randomID.getRandomID());
        return user;
    }

    /**
     * Takes a register request and fills out generational data for that user
     * @param request
     * @return Register Results
     */
    public RegisterResult register(RegisterRequest request) {
        try {
            openConnection();
            userDAO.insert(getUser(request));
            String authToken = randomID.getRandomID();
            AuthToken token = new AuthToken(authToken, request.getUsername());
            authTokenDAO.insert(token);
            db.closeConnection(true);
            FillService fillService = new FillService();
            fillService.fill(new FillRequest(request.getUsername()));
            db.openConnection();
            userDAO = new UserDAO(db.getConnection());
            String personID = userDAO.find(request.getUsername()).getPersonID();
            db.closeConnection(false);
            return new RegisterResult(token.getAuth_token(), request.getUsername(), personID, true);
        }
        catch (DataAccessException e) {
            return new RegisterResult(false, "Error: Could not register");
        }
        finally {
            try {
                if(db.getConnection() != null)
                db.closeConnection(false);
            }
            catch (DataAccessException e) {
                e.printStackTrace();
            }

        }

    }


}

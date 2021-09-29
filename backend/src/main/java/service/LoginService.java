package service;

import RandomGenerator.RandomID;
import Request.LoginRequest;
import Result.LoginResult;
import dao.AuthTokenDAO;
import dao.DataAccessException;
import dao.Database;
import dao.UserDAO;
import model.AuthToken;
import model.User;

import javax.xml.crypto.Data;

public class LoginService {
    private Database db;

    /**
     * makes request to start a user's session and create a new authToken
     * @param loginRequest
     * @return loginResult
     */

    public LoginResult login(LoginRequest loginRequest) {
        RandomID randomID = new RandomID();
        try {
            db = new Database();
            db.openConnection();
            UserDAO userDAO = new UserDAO(db.getConnection());
            User user = userDAO.find(loginRequest.getUsername());

            if(user == null || !user.getPassword().equals(loginRequest.getPassword())){
                return new LoginResult(false, "Error: invalid login credentials");
            }
            AuthToken authToken = new AuthToken(randomID.getRandomID(), user.getUsername());
            AuthTokenDAO authTokenDAO = new AuthTokenDAO(db.getConnection());
            authTokenDAO.insert(authToken);
            db.closeConnection(true);
            return new LoginResult(true, authToken, user.getPersonID());


        }
        catch (DataAccessException e) {
            return new LoginResult(false, "Error logging in");
        }
        finally {
            try {
                if(db.getConnection() != null)
                    db.closeConnection(false);
            }
            catch (DataAccessException exception) {
                exception.printStackTrace();
            }
        }
    }

}

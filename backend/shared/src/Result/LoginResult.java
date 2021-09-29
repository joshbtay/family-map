package Result;

import model.AuthToken;

public class LoginResult extends Result{
    private String authtoken;
    private String username;
    private String personID;


    public LoginResult(boolean success, AuthToken authToken, String personID) {
        super(success);
        this.authtoken = authToken.getAuth_token();
        this.username = authToken.getUsername();
        this.personID = personID;
    }

    public LoginResult(boolean success, String error) {
        super(success, error);
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public String getUsername() {
        return username;
    }

    public String getPersonID() {
        return personID;
    }
}

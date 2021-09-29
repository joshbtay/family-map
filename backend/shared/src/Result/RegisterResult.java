package Result;

import model.AuthToken;

public class RegisterResult extends Result{
    private String authtoken;
    private String username;
    private String personID;


    public RegisterResult(String authtoken, String username, String personID, boolean success) {
        super(success);
        this.username = username;
        this.authtoken = authtoken;
        this.personID = personID;
    }

    public RegisterResult(boolean success, String error) {
        super(success, error);
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

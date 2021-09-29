package model;

public class AuthToken {
    private String auth_token;
    private String username;

    public AuthToken(String auth_token, String username) {
        this.auth_token = auth_token;
        this.username = username;
    }

    public String getAuth_token() {
        return auth_token;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}

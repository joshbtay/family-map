package model;

public class User {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private String personID;

    public User(String username, String password, String email, String first_name, String last_name, String gender, String person_id) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = first_name;
        this.lastName = last_name;
        this.gender = gender;
        this.personID = person_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPerson_id(String person_id) {
        this.personID = person_id;
    }
}

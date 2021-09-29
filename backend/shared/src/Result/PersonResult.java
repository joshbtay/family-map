package Result;

import model.Person;

import java.util.ArrayList;

public class PersonResult extends Result{
    private ArrayList<Person> data;

    private String associatedUsername;
    private String personID;
    private String firstName;
    private String lastName;
    private String gender;
    private String fatherID;
    private String motherID;
    private String spouseID;


    public PersonResult(boolean success, Person person) {
        super(success);
        this.associatedUsername = person.getAssociatedUsername();
        this.personID = person.getPersonID();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.gender = person.getGender();
        this.fatherID = person.getFatherID();
        this.motherID = person.getMotherID();
        this.spouseID = person.getSpouseID();
    }

    public PersonResult(boolean success, ArrayList<Person> data) {
        super(success);
        this.data = data;
    }

    public PersonResult(boolean success, String error) {
        super(success, error);
    }

    public ArrayList<Person> getPersons() {
        return data;
    }

    public void setPersons(ArrayList<Person> data) {
        this.data = data;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public String getPersonID() {
        return personID;
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

    public String getFatherID() {
        return fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }
}

package Result;

import model.Event;

import java.util.ArrayList;

public class EventResult extends Result{
    private String associatedUsername;
    private String eventID;
    private String personID;
    private float latitude;
    private float longitude;
    private String country;
    private String city;
    private String eventType;
    private int year;

    private ArrayList<Event> data;

    public EventResult(boolean success, Event result) {
        super(success);
        this.associatedUsername = result.getUsername();
        this.eventID = result.getEventID();
        this.personID = result.getPersonID();
        this.latitude = result.getLatitude();
        this.longitude = result.getLongitude();
        this.country = result.getCountry();
        this.city = result.getCity();
        this.eventType = result.getEventType();
        this.year = result.getYear();
    }

    public EventResult(boolean success, ArrayList<Event> data) {
        super(success);
        this.data = data;
    }

    public EventResult(boolean success, String error) {
        super(success, error);
    }

    public ArrayList<Event> getResults() {
        return data;
    }

    public void setResults(ArrayList<Event> data) {
        this.data = data;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public String getEventID() {
        return eventID;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getEventType() {
        return eventType;
    }

    public int getYear() {
        return year;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
}

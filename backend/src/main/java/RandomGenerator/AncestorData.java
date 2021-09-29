package RandomGenerator;

import model.Event;
import model.Person;

public class AncestorData {
    public Person[] getPersons() {
        return persons;
    }

    public void setAncestors(Person[] persons) {
        this.persons = persons;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }

    private Person[] persons;
    private Event[] events;

    public AncestorData(Person[] persons, Event[] events) {
        this.persons = persons;
        this.events = events;
    }
}

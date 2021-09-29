package com.example.familymap;

import android.os.Handler;

import com.example.familymap.core.DataCache;
import com.example.familymap.core.FamilyMember;
import com.example.familymap.web.LoginTask;
import com.example.familymap.web.Proxy;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

import Request.EventRequest;
import Request.LoginRequest;
import Request.PersonRequest;
import Result.EventResult;
import Result.LoginResult;
import Result.PersonResult;
import model.Event;
import model.Person;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ModelTest {
    DataCache dataCache = DataCache.getInstance();
    String username = "sheila";
    String password = "parker";
    String server = "10.5.205.3:8080";
    String father = "Blaine_McGary";
    String child = "Sheila_Parker";
    String mother = "Betty_White";
    String spouse = "Davis_Hyer";
    Proxy proxy = Proxy.getInstance();

    //I had @BeforeEach but for some reason it wasn't working, so I just call it separately.
    public void setup(){
        dataCache.clear();
        proxy.setServer(server);
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult result = proxy.login(loginRequest);
        if(result.getMessage() == null) {
            PersonResult personResult = proxy.person(new PersonRequest(result.getAuthtoken()));
            DataCache dataCache = DataCache.getInstance();
            dataCache.setPeople(personResult);
            EventResult eventResult = proxy.event(new EventRequest(result.getAuthtoken()));
            dataCache.setEvents(eventResult);
            dataCache.setUser(result.getPersonID());
        }
    }
    //Log in with alternative username
    public void setup(String username, String password){
        dataCache.clear();
        proxy.setServer(server);
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult result = proxy.login(loginRequest);
        if(result.getMessage() == null) {
            PersonResult personResult = proxy.person(new PersonRequest(result.getAuthtoken()));
            DataCache dataCache = DataCache.getInstance();
            dataCache.setPeople(personResult);
            EventResult eventResult = proxy.event(new EventRequest(result.getAuthtoken()));
            dataCache.setEvents(eventResult);
            dataCache.setUser(result.getPersonID());
        }
    }

    @Test
    public void testFamilyRelations() {
        setup();
        assertEquals(8, dataCache.getPeople().size());
        assertEquals(child, dataCache.getPerson(father).getChildren().get(0));
        assertEquals(1, dataCache.getPerson(father).getChildren().size());
        assertEquals(father, dataCache.getPerson(child).getFatherID());
        assertEquals(mother, dataCache.getPerson(child).getMotherID());
        assertEquals(child, dataCache.getPerson(mother).getChildren().get(0));
        assertEquals(father, dataCache.getPerson(mother).getSpouseID());
        assertEquals(mother, dataCache.getPerson(father).getSpouseID());
        assertEquals(spouse, dataCache.getPerson(child).getSpouseID());
        assertEquals(child, dataCache.getPerson(spouse).getSpouseID());
    }

    @Test
    public void testNoFamilyRelations() {
        String username = "patrick";
        String password = "spencer";
        setup(username,password);
        assertEquals(3, dataCache.getPeople().size());
        assertEquals(0, dataCache.getPerson("Patrick_Spencer").getChildren().size());
        assertNull(dataCache.getPerson("Patrick_Spencer").getSpouseID());
        assertNull(dataCache.getPerson("Happy_Birthday").getFatherID());
        assertNull(dataCache.getPerson("Happy_Birthday").getMotherID());
        assertNull(dataCache.getPerson("Golden_Boy").getFatherID());
        assertNull(dataCache.getPerson("Golden_Boy").getMotherID());
    }

    @Test
    public void testFilter(){
        setup();
        assertEquals(16, dataCache.getFilteredEvents().size());
        dataCache.setMaleEvents(false);
        assertEquals(10, dataCache.getFilteredEvents().size());
        dataCache.setFemaleEvents(false);
        dataCache.setMaleEvents(true);
        assertEquals(6, dataCache.getFilteredEvents().size());
        dataCache.setFemaleEvents(true);
        dataCache.setFatherSide(false);
        assertEquals(11, dataCache.getFilteredEvents().size());
        dataCache.setMotherSide(false);
        assertEquals(6, dataCache.getFilteredEvents().size());
        dataCache.setFatherSide(true);
        assertEquals(11, dataCache.getFilteredEvents().size());
    }

    @Test
    public void testFilterEmpty(){
        setup();
        assertEquals(16, dataCache.getFilteredEvents().size());
        dataCache.setMaleEvents(false);
        dataCache.setFemaleEvents(false);
        assertNull(dataCache.getFilteredEvents());
        dataCache.setFemaleEvents(true);
        dataCache.setMaleEvents(true);
        assertEquals(16, dataCache.getFilteredEvents().size());
    }

    @Test
    public void testSortedEvents(){
        setup();
        assertEquals(16, dataCache.getFilteredEvents().size());
        List<Event> lifeEvents = dataCache.getEvents(child);
        assertEquals(5, lifeEvents.size());
        assertEquals(1970, lifeEvents.get(0).getYear());
        assertEquals(2012, lifeEvents.get(1).getYear());
        assertEquals(2014, lifeEvents.get(2).getYear());
        assertEquals(2014, lifeEvents.get(3).getYear());
        assertEquals(2015, lifeEvents.get(4).getYear());
    }

    @Test
    public void testSortedEventsSecond(){
        setup();
        assertEquals(16, dataCache.getFilteredEvents().size());
        List<Event> lifeEvents = dataCache.getEvents("Ken_Rodham");
        assertEquals(2, lifeEvents.size());
        assertEquals(1879, lifeEvents.get(0).getYear());
        assertEquals(1895, lifeEvents.get(1).getYear());
    }

    @Test
    public void testSearch(){
        String query = "a";
        setup();
        List<Event> events = dataCache.searchEvents(query);
        assertEquals(16, events.size());
        dataCache.setMaleEvents(false);
        events = dataCache.searchEvents(query);
        assertEquals(10, events.size());
        List<FamilyMember> people = dataCache.searchPeople(query);
        assertEquals(6, people.size());
        dataCache.setMaleEvents(true);
        query = "com";
        events = dataCache.searchEvents(query);
        assertEquals(2, events.size());
        query = "20";
        events = dataCache.searchEvents(query);
        assertEquals(7, events.size());
    }

    @Test
    public void testSearchEmpty(){
        String query = "";
        List<Event> events = dataCache.searchEvents(query);
        assertEquals(0, events.size());
        List<FamilyMember> people = dataCache.searchPeople(query);
        assertEquals(0, people.size());
        query = "XYZ";
        events = dataCache.searchEvents(query);
        assertEquals(0, events.size());
        people = dataCache.searchPeople(query);
        assertEquals(0, people.size());
    }
}

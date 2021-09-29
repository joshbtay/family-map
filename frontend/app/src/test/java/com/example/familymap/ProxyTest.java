package com.example.familymap;

import com.example.familymap.web.Proxy;

import org.junit.Test;
import org.junit.*;

import Request.EventRequest;
import Request.LoginRequest;
import Request.PersonRequest;
import Result.EventResult;
import Result.LoginResult;
import Result.PersonResult;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ProxyTest {
    Proxy proxy = Proxy.getInstance();

    @Before
    public void beforeTest(){
        proxy.setServer("10.5.205.3:8080");
    }

    @Test
    public void LoginSuccess() {
        String username = "sheila";
        String password = "parker";
        String personID = "Sheila_Parker";
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult loginResult = proxy.login(loginRequest);
        assertTrue(loginResult.getSuccess());
        assertEquals(personID, loginResult.getPersonID());
        assertEquals(username, loginResult.getUsername());
    }

    @Test
    public void LoginFail() {
        String username = "shiela";
        String password = "parker";
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult loginResult = proxy.login(loginRequest);
        assertFalse(loginResult.getSuccess());
        assertNull(loginResult.getUsername());
        assertNull(loginResult.getPersonID());
        assertNull(loginResult.getAuthtoken());
    }

    @Test
    public void getUserSuccess() {
        String username = "sheila";
        String password = "parker";
        String personID = "Sheila_Parker";
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult loginResult = proxy.login(loginRequest);
        assertTrue(loginResult.getSuccess());
        PersonResult personResult = proxy.person(new PersonRequest(loginResult.getAuthtoken()));
        assertTrue(personResult.getSuccess());
        assertEquals(personID, personResult.getPersons().get(0).getPersonID());
        assertEquals(username, personResult.getPersons().get(0).getAssociatedUsername());
    }

    @Test
    public void getUserFail() {
        String username = "sheila";
        String password = "parker";
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult loginResult = proxy.login(loginRequest);
        assertTrue(loginResult.getSuccess());
        PersonResult personResult = proxy.person(new PersonRequest(loginResult.getAuthtoken()));
        assertNull(personResult.getFatherID());
        assertNull(personResult.getFirstName());
        assertNull(personResult.getAssociatedUsername());
        assertNull(personResult.getGender());
        assertTrue(personResult.getSuccess());
    }

    @Test
    public void getPeopleFirst() {
        String username = "sheila";
        String password = "parker";
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult loginResult = proxy.login(loginRequest);
        assertTrue(loginResult.getSuccess());
        PersonResult personResult = proxy.person(new PersonRequest(loginResult.getAuthtoken()));
        assertEquals(8, personResult.getPersons().size());
        assertEquals("Mrs_Jones",
                personResult.getPersons().get(personResult.getPersons().size() - 1).getPersonID());
        assertEquals("Mrs",
                personResult.getPersons().get(personResult.getPersons().size() - 1).getFirstName());
        assertEquals("Jones",
                personResult.getPersons().get(personResult.getPersons().size() - 1).getLastName());
    }

    @Test
    public void getPeopleSecond() {
        String username = "patrick";
        String password = "spencer";
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult loginResult = proxy.login(loginRequest);
        assertTrue(loginResult.getSuccess());
        PersonResult personResult = proxy.person(new PersonRequest(loginResult.getAuthtoken()));
        assertEquals(3, personResult.getPersons().size());
        assertEquals("Golden_Boy",
                personResult.getPersons().get(personResult.getPersons().size() - 1).getPersonID());
        assertEquals("Spencer",
                personResult.getPersons().get(personResult.getPersons().size() - 1).getFirstName());
        assertEquals("Seeger",
                personResult.getPersons().get(personResult.getPersons().size() - 1).getLastName());
    }

    @Test
    public void getEventsSuccess() {
        String username = "sheila";
        String password = "parker";
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult loginResult = proxy.login(loginRequest);
        assertTrue(loginResult.getSuccess());
        EventRequest eventRequest = new EventRequest(loginResult.getAuthtoken());
        EventResult eventResult = proxy.event(eventRequest);
        assertTrue(eventResult.getSuccess());
        assertNull(eventResult.getAssociatedUsername());
        assertEquals(16, eventResult.getResults().size());
        assertEquals("birth", eventResult.getResults().get(0).getEventType());
        assertEquals("sheila", eventResult.getResults().get(0).getUsername());
        assertEquals("Mrs_Jones",
                eventResult.getResults().get(eventResult.getResults().size() - 1).getPersonID());
    }

    @Test
    public void getEventsFail() {
        String username = "sheila";
        String password = "parker";
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult loginResult = proxy.login(loginRequest);
        assertTrue(loginResult.getSuccess());
        EventRequest eventRequest = new EventRequest(username);
        EventResult eventResult = proxy.event(eventRequest);
        assertFalse(eventResult.getSuccess());
        assertNull(eventResult.getResults());
        assertNull(eventResult.getAssociatedUsername());
        assertNull(eventResult.getEventID());
    }
}
package com.example.familymap.core;

import android.util.Log;
import android.util.Pair;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Result.EventResult;
import Result.PersonResult;
import model.Event;
import model.Person;

public class DataCache {
    private static DataCache instance = new DataCache();
    public static DataCache getInstance() {
        return instance;
    }

    private Map<String, FamilyMember> people;
    private Map<String, Event> events;
    private Map<String, List<Event>> personEvents;
    private Map<String, FamilyMember> paternalFamily;
    private Map<String, FamilyMember> maternalFamily;
    private FamilyMember user;

    private Boolean lifeLine;
    private Boolean familyLine;
    private Boolean spouseLine;
    private Boolean fatherSide;
    private Boolean motherSide;
    private Boolean maleEvents;
    private Boolean femaleEvents;

    private DataCache() {
        this.clear();
    }

    public Map<String, FamilyMember> getPeople() {
        return people;
    }

    public void setPeople(PersonResult result) {
        for (Person person: result.getPersons()){
            this.people.put(person.getPersonID(), new FamilyMember(person));
        }
        for (Map.Entry<String,FamilyMember> person: people.entrySet()){
            String fatherID = person.getValue().getFatherID();
            String motherID = person.getValue().getMotherID();
            if(fatherID != null & people.containsKey(fatherID)){
                people.get(fatherID).addChild(person.getKey());
            }
            if(motherID != null & people.containsKey(motherID)){
                people.get(motherID).addChild(person.getKey());
            }
        }

    }

    private void searchFamily(boolean side, String personID){
        if(personID == null || personID.equals("")){
            return;
        }
        String fatherID = people.get(personID).getFatherID();
        if(people.containsKey(fatherID)){
            searchFamily(side, fatherID);
        }
        String motherID = people.get(personID).getMotherID();
        if(people.containsKey(motherID)){
            searchFamily(side, motherID);
        }
        if(side){
            paternalFamily.put(personID, people.get(personID));
        }
        else maternalFamily.put(personID, people.get(personID));
    }

    public FamilyMember getUser() {
        return user;
    }

    public void setUser(String personID) {
        this.user = people.get(personID);
        searchFamily(true, user.getFatherID());
        searchFamily(false, user.getMotherID());
    }

    public void setEvents(EventResult eventResult) {
        for (Event event : eventResult.getResults()){
            this.events.put(event.getEventID(), event);
            if(!personEvents.containsKey(event.getPersonID()))
                personEvents.put(event.getPersonID(), new ArrayList<Event>());
            personEvents.get(event.getPersonID()).add(event);
        }
    }

    public Event getEvent(String eventID) {
        return events.get(eventID);
    }

    public FamilyMember getPerson(String personID) {
        return people.get(personID);
    }

    public List<Event> getEvents(String personID) {
        Map<String,Event> filtered = getFilteredEvents();
        List<Event> specificEvents = new ArrayList<>();
        if(personEvents.get(personID) == null){
            return specificEvents;
        }
        for(Event event : personEvents.get(personID)){
            if (filtered.containsKey(event.getEventID())){
                specificEvents.add(event);
            }
        }
        Collections.sort(specificEvents, new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return Integer.compare(o1.getYear(), o2.getYear());
            }
        });

        return specificEvents;
    }

    public float getColor(String eventType) {
        return Math.abs(eventType.hashCode() % 360);
    }

    public Boolean getLifeLine() {
        return lifeLine;
    }

    public void setLifeLine(Boolean lifeLine) {
        this.lifeLine = lifeLine;
    }

    public Boolean getFamilyLine() {
        return familyLine;
    }

    public void setFamilyLine(Boolean familyLine) {
        this.familyLine = familyLine;
    }

    public Boolean getSpouseLine() {
        return spouseLine;
    }

    public void setSpouseLine(Boolean spouseLine) {
        this.spouseLine = spouseLine;
    }

    public Boolean getFatherSide() {
        return fatherSide;
    }

    public void setFatherSide(Boolean fatherSide) {
        this.fatherSide = fatherSide;
    }

    public Boolean getMotherSide() {
        return motherSide;
    }

    public void setMotherSide(Boolean motherSide) {
        this.motherSide = motherSide;
    }

    public Boolean getMaleEvents() {
        return maleEvents;
    }

    public void setMaleEvents(Boolean maleEvents) {
        this.maleEvents = maleEvents;
    }

    public Boolean getFemaleEvents() {
        return femaleEvents;
    }

    public void setFemaleEvents(Boolean femaleEvents) {
        this.femaleEvents = femaleEvents;
    }

    public Map<String,Event> getFilteredEvents(){
        if(!maleEvents & !femaleEvents){
            return null;
        }
        Map<String,Event> filtered = new HashMap<>();
        if(fatherSide & motherSide){
            for(Map.Entry<String,Event> event : events.entrySet()){
                filtered.put(event.getKey(), event.getValue());
            }
        }
        else{
            if(!fatherSide & !motherSide){
                //
            }
            else if(!motherSide){
                for(Map.Entry<String,FamilyMember> person: paternalFamily.entrySet()){
                    List<Event> currentEvents = personEvents.get(person.getKey());
                    for (Event event : currentEvents){
                        filtered.put(event.getEventID(), event);
                    }

                }
            }
            else if (!fatherSide) {
                for(Map.Entry<String,FamilyMember> person: maternalFamily.entrySet()){
                    List<Event> currentEvents = personEvents.get(person.getKey());
                    for (Event event : currentEvents){
                        filtered.put(event.getEventID(), event);
                    }
                }
            }
            for(Event event : personEvents.get(user.getPersonID())){
                filtered.put(event.getEventID(), event);
            }
            if(user.getSpouseID() != null & personEvents.containsKey(user.getSpouseID())){
                for(Event event : personEvents.get(user.getSpouseID())){
                    filtered.put(event.getEventID(), event);
                }
            }
        }
        List<String> removal = new ArrayList<>();
        if(!maleEvents){
            for(Map.Entry<String,Event> event : filtered.entrySet()){
                if(people.get(event.getValue().getPersonID())!=null ){
                    if (people.get(event.getValue().getPersonID()).getGender().toLowerCase().equals("m")) {
                        removal.add(event.getKey());
                    }
                }
            }
        }
        else if(!femaleEvents){
            for(Map.Entry<String,Event> event : filtered.entrySet()){
                if(people.get(event.getValue().getPersonID())!=null ){
                    if (people.get(event.getValue().getPersonID()).getGender().toLowerCase().equals("f")) {
                        removal.add(event.getKey());
                    }
                }
            }
        }

        for(String key: removal){
            filtered.remove(key);
        }
        return filtered;
    }

    public void clear() {
        people = new HashMap<>();
        events = new HashMap<>();
        personEvents = new HashMap<>();
        paternalFamily = new HashMap<>();
        maternalFamily = new HashMap<>();
        user = null;
        lifeLine = true;
        familyLine = true;
        spouseLine = true;
        fatherSide = true;
        motherSide = true;
        maleEvents = true;
        femaleEvents = true;
    }

    public ArrayList<ArrayList<Pair<LatLng, LatLng>>> getFirstEvents(Event event) {
        FamilyMember mother = people.get(people.get(event.getPersonID()).getMotherID());
        FamilyMember father = people.get(people.get(event.getPersonID()).getFatherID());
        ArrayList<ArrayList<Pair<LatLng,LatLng>>> firstEvents = new ArrayList<>();
        findAncestors(event, mother, firstEvents, 0);
        findAncestors(event, father, firstEvents, 0);
        return firstEvents;
    }

    private void findAncestors(Event childEvent,FamilyMember parent, ArrayList<ArrayList<Pair<LatLng,LatLng>>> firstEvents, int generation){
        if(childEvent == null || parent == null) return;
        List<Event> events = getEvents(parent.getPersonID());
        if(events.size() == 0) return;
        if(firstEvents.size() <= generation){
            firstEvents.add(new ArrayList<>());
        }
        LatLng first = new LatLng(childEvent.getLatitude(), childEvent.getLongitude());
        LatLng second = new LatLng(events.get(0).getLatitude(), events.get(0).getLongitude());
        firstEvents.get(generation).add(new Pair<LatLng, LatLng>(first,second));
        findAncestors(events.get(0), people.get(parent.getMotherID()), firstEvents, generation+1);
        findAncestors(events.get(0), people.get(parent.getFatherID()), firstEvents, generation+1);
    }

    public List<FamilyMember> searchPeople(String param){
        List<FamilyMember> result = new LinkedList<>();
        if(param.equals("")) return result;
        if(getPeople() == null) return result;
        for(Map.Entry<String, FamilyMember> entry : getPeople().entrySet()){
            FamilyMember person = entry.getValue();
            param = param.toLowerCase().replaceAll(" ","");
            if(person.getLastName().toLowerCase().contains(param)|
                    person.getFirstName().toLowerCase().contains(param)|
                    (person.getFirstName() + person.getLastName()).contains(param)){
                result.add(person);
            }
        }
        return result;
    }

    public List<Event> searchEvents(String param) {
        List<Event> result = new LinkedList<>();
        if(param.equals("")) return result;
        if(getFilteredEvents() == null) return result;
        for(Map.Entry<String, Event> entry : getFilteredEvents().entrySet()){
            param = param.toLowerCase().replaceAll(" ","");
            Event event = entry.getValue();
            if(event.getEventType().toLowerCase().contains(param)|
                    event.getCountry().toLowerCase().contains(param)|
                    event.getCity().toLowerCase().contains(param)|
                    Integer.toString(event.getYear()).contains(param)){
                result.add(event);
            }
        }
        return result;
    }
}

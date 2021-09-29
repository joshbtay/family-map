package com.example.familymap.core;


import java.util.ArrayList;

import model.Person;

public class FamilyMember extends Person {
    private ArrayList<String> children = new ArrayList<>();
    public FamilyMember(String personID, String associatedUsername, String firstName,
                        String lastName, String gender, String fatherID,
                        String motherID, String spouseID) {
        super(personID, associatedUsername, firstName, lastName,
                gender, fatherID, motherID, spouseID);
    }

    public FamilyMember(Person person) {
        super(person.getPersonID(), person.getAssociatedUsername(), person.getFirstName(),
                person.getLastName(), person.getGender(), person.getFatherID(),
                person.getMotherID(), person.getSpouseID());
    }

    public void setChildren(ArrayList<String> children) {
        this.children = children;
    }
    public void addChild(String child){
        children.add(child);
    }

    public ArrayList<String> getChildren(){
        return children;
    }
}

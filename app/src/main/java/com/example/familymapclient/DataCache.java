package com.example.familymapclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.Event;
import model.Person;

public class DataCache {


    private static DataCache instance = new DataCache();

    public static DataCache getInstance() {
        return instance;
    }

    public DataCache() {

    }



    HashMap<String, Float> colorShade = new HashMap<>();

    float color = 0;

    public float getColorMap(String eventName) {

        if (colorShade.containsKey(eventName)) {
            return colorShade.get(eventName);
        }
        else {
            color += 32;
            colorShade.put(eventName, color);
            return colorShade.get(eventName);
        }
    }

    boolean spouseSwitch = true;

    public void setSpouseSwitch(boolean value) {
        spouseSwitch = value;
    }

    public boolean getSpouseSwitch() {
        return spouseSwitch;
    }

    boolean lifeStoryLines = true;

    public void setStorySwitch(boolean value) {
        lifeStoryLines = value;
    }

    public boolean getStorySwitch() {
        return lifeStoryLines;
    }


    List<Person> people = new ArrayList<Person>();

    public List<Person> getPeople() {
        return people;
    }

    void setPerson(Person person) {
        people.add(person);
    }

    List<Event> events = new ArrayList<Event>();

    public List<Event> getEvents() {
        return events;
    }

    void setEvent(Event event) {
        events.add(event);
    }


    List<Person> getPerson() {
        return people;
    }

    List<Event> getEvent() {
        return events;
    }


    //function that returns a list of events
    //GetPersonsEvents
    //Take in person id and returns all the events

    public List<Event> getPersonsEvent(String personID) {
        List<Event> personIDEvents = new ArrayList<>();

        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getPersonID().equals(personID)) {
                personIDEvents.add(events.get(i));

            }
        }
        return personIDEvents;
    }

    public List<List> getFamilyMembers(String personID) {

        List<List> finalList = new ArrayList<>();
        List<Person> nameList = new ArrayList<>();
        List<String> titleList = new ArrayList<>();
        Person currentPerson = null;

        for (int i = 0; i < people.size(); i++) {
            if (people.get(i).getPersonID().equals(personID)) {
                currentPerson = people.get(i);
            }
        }


        String motherID = currentPerson.getMotherID();
        String fatherID = currentPerson.getFatherID();
        String spouseID = currentPerson.getSpouseID();

        if (motherID != null) {
            titleList.add("Mother");
            for (int i = 0; i < people.size(); i++) {
                if (people.get(i).getPersonID().equals(motherID)) {
                    nameList.add(people.get(i));
                }
            }
        }

        if (fatherID != null) {
            titleList.add("Father");
            for (int i = 0; i < people.size(); i++) {
                if (people.get(i).getPersonID().equals(fatherID)) {
                    nameList.add(people.get(i));
                }
            }
        }

        if (spouseID != null) {
            titleList.add("Spouse");
            for (int i = 0; i < people.size(); i++) {
                if (people.get(i).getSpouseID().equals(currentPerson.getPersonID())) {
                    nameList.add(people.get(i));
                }
            }
        }

        for (int i = 0; i < people.size(); i++) {
            if (people.get(i).getMotherID() == null || people.get(i).getFatherID() == null) {
                continue;
            }
            else if (people.get(i).getFatherID().equals(currentPerson.getPersonID()) || people.get(i).getMotherID().equals(currentPerson.getPersonID())) {
                titleList.add("Child");
                nameList.add(people.get(i));
                break;
            }
        }


        finalList.add(titleList);
        finalList.add(nameList);

        return finalList;
    }


}

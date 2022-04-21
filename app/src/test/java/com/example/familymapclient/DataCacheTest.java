package com.example.familymapclient;

import static org.junit.Assert.*;

import android.provider.ContactsContract;

import org.junit.Test;

import java.util.List;

import model.Event;
import model.Person;
import org.junit.*;

public class DataCacheTest {


    @Test
    public void setSpouseSwitchTest() {

        DataCache dataCache = new DataCache();

        dataCache.setSpouseSwitch(true);
        boolean  value = dataCache.getSpouseSwitch();

        assertTrue(value);
    }

    @Test
    public void getColorMapTest() {

        DataCache dataCache = new DataCache();

        float blue = dataCache.getColorMap("Riding a bike");
        int blueValue = (int) blue;
        assertEquals(blueValue, 32);

        float red = dataCache.getColorMap("Going for a walk");
        int redValue = (int) red;
        assertEquals(redValue, 64);

        float redAgain = dataCache.getColorMap("Going for a walk");
        int redRepeat = (int) redAgain;
        assertEquals(redRepeat, 64);

        float yellow = dataCache.getColorMap("Finishing 240 project");
        int yellowValue = (int) yellow;
        assertEquals(yellowValue, 96);

    }

    @Test
    public void setLifeStoryLinesTest() {

        DataCache dataCache = new DataCache();

        dataCache.setStorySwitch(true);
        boolean trueValue =dataCache.getStorySwitch();
        assertTrue(trueValue);

        dataCache.setStorySwitch(false);
        boolean falseValue =dataCache.getStorySwitch();
        assertFalse(falseValue);

    }

    @Test
    public void personListTest() {

        DataCache dataCache = new DataCache();

        Person Sheila = new Person("sheila_1234", "SheilaParker",
                "Sheila", "Parker", "f",
                "dad1234", "mom1234", "husband1234");

        Person Patrick = new Person("patrick_1234", "PatrickSpencer",
                "Patrick", "Spencer", "m",
                "dad1234", "mom1234", "wife1234");


        List<Person> peopleList = dataCache.getPerson();

        dataCache.setPerson(Sheila);

        assertEquals(peopleList.size(), 1);
        assertEquals(peopleList.get(0).getPersonID(), "sheila_1234");

        dataCache.setPerson(Patrick);

        assertEquals(peopleList.size(), 2);
        assertEquals(peopleList.get(1).getPersonID(), "patrick_1234");

    }

    @Test
    public void eventListTest() {

        DataCache dataCache = new DataCache();

        Event birth = new Event("birth_1234", "SheilaParker",
                "Sheila", 10293.423f, 124000.1f,
                "USA", "Draper", "birth", 1997);

        Event death= new Event("death_1234", "PatrickSpencer",
                "Patrick", 902.1f, 423.352f,
                "Argetina", "Cordoba", "death", 1776);

        List<Event> eventList = dataCache.getEvent();

        dataCache.setEvent(birth);

        assertEquals(eventList.size(), 1);
        assertEquals(eventList.get(0).getEventID(), "birth_1234");

        dataCache.setEvent(death);

        assertEquals(eventList.size(), 2);
        assertEquals(eventList.get(1).getEventID(), "death_1234");

    }
    
    @Test
    public void getPersonTest() {

        DataCache dataCache = new DataCache();

        Person Sheila = new Person("sheila_1234", "SheilaParker",
                "Sheila", "Parker", "f",
                "dad1234", "mom1234", "husband1234");

        Person Patrick = new Person("patrick_1234", "PatrickSpencer",
                "Patrick", "Spencer", "m",
                "dad1234", "mom1234", "wife1234");


        List<Person> peopleList = dataCache.getPerson();

        dataCache.setPerson(Sheila);
        dataCache.setPerson(Patrick);

        assertEquals(peopleList.size(), 2);
        
        assertEquals(peopleList.get(0).getPersonID(), "sheila_1234");
        assertEquals(peopleList.get(1).getPersonID(), "patrick_1234");
        
        assertEquals(peopleList.get(0).getGender(), "f");
        assertEquals(peopleList.get(1).getGender(), "m");
        
        assertEquals(peopleList.get(0).getAssociatedUsername(), "SheilaParker");
        assertEquals(peopleList.get(1).getAssociatedUsername(), "PatrickSpencer");

        assertEquals(peopleList.get(0).getAssociatedUsername(), "SheilaParker");
        assertEquals(peopleList.get(1).getAssociatedUsername(), "PatrickSpencer");

        
    }

    @Test
    public void getEventTest() {

        DataCache dataCache = new DataCache();

        Event birth = new Event("birth_1234", "SheilaParker",
                "Sheila", 10293.423f, 124000.1f,
                "USA", "Draper", "birth", 1997);

        Event death= new Event("death_1234", "PatrickSpencer",
                "Patrick", 902.1f, 423.352f,
                "Argentina", "Cordoba", "death", 1776);

        List<Event> eventList = dataCache.getEvent();

        dataCache.setEvent(birth);
        dataCache.setEvent(death);

        assertEquals(eventList.size(), 2);

        assertEquals(eventList.get(0).getEventID(), "birth_1234");
        assertEquals(eventList.get(1).getEventID(), "death_1234");

        assertEquals(eventList.get(0).getCountry(), "USA");
        assertEquals(eventList.get(1).getCountry(), "Argentina");

        assertEquals(eventList.get(0).getEventType(), "birth");
        assertEquals(eventList.get(1).getEventType(), "death");


    }

    @Test
    public void getPersonEventTest() {

        DataCache dataCache = new DataCache();

        Person Sheila = new Person("sheila_1234", "SheilaParker",
                "Sheila", "Parker", "f",
                "dad1234", "mom1234", "husband1234");

        Person Patrick = new Person("patrick_1234", "PatrickSpencer",
                "Patrick", "Spencer", "m",
                "dad1234", "mom1234", "wife1234");

        Event birth = new Event("birth_1234", "SheilaParker",
                "patrick_1234", 10293.423f, 124000.1f,
                "USA", "Draper", "birth", 1997);

        Event death= new Event("death_1234", "PatrickSpencer",
                "patrick_1234", 902.1f, 423.352f,
                "Argetina", "Cordoba", "death", 1776);

        dataCache.setEvent(birth);
        dataCache.setEvent(death);
        dataCache.setPerson(Patrick);

        List<Event> eventsList = dataCache.getPersonsEvent("sheila_1234");

        assertEquals(eventsList.size(), 0);

        List<Event> moreEventsList = dataCache.getPersonsEvent("patrick_1234");

        assertEquals(moreEventsList.size(), 2);

    }

    @Test
    public void getFamilyMembersTest() {

        DataCache dataCache = new DataCache();

        Person Sheila = new Person("sheila_1234", "SheilaParker",
                "Sheila", "Parker", "f",
                "dad1234", "mom1234", "husband1234");

        Person Patrick = new Person("patrick_1234", "PatrickSpencer",
                "Patrick", "Spencer", "m",
                "dad1234", "mom1234", "wife1234");

        Event birth = new Event("birth_1234", "SheilaParker",
                "patrick_1234", 10293.423f, 124000.1f,
                "USA", "Draper", "birth", 1997);

        Event death= new Event("death_1234", "PatrickSpencer",
                "patrick_1234", 902.1f, 423.352f,
                "Argetina", "Cordoba", "death", 1776);

        dataCache.setEvent(birth);
        dataCache.setEvent(death);
        dataCache.setPerson(Patrick);

        List<List> myList = dataCache.getFamilyMembers("patrick_1234");

        assertNotNull(myList);
        assertEquals(myList.size(), 2);
        assertEquals(myList.get(0).size(), 3);
        assertEquals(myList.get(1).size(), 0);

    }



}

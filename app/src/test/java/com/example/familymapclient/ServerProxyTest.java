package com.example.familymapclient;

import static org.junit.Assert.*;

import org.junit.Test;

import Request.LoginRequest;
import Request.RegisterRequest;
import Result.AllEventResult;
import Result.LoginResult;
import Result.PersonFamilyResult;
import Result.RegisterResult;
import model.AuthToken;

public class ServerProxyTest {


    @Test
    public void loginTest() {

        ServerProxy serverProxy = new ServerProxy();
        LoginRequest request = new LoginRequest("sheila", "parker");
        String host = "localhost";
        String port = "8080";

        LoginResult result = serverProxy.login(request, host, port);

        //assertNull(result);
        assertEquals(result.getPersonID(),"Sheila_Parker");
        assertEquals(result.getUsername(), "sheila");


    }

    @Test
    public void registerResultTest() {


        ServerProxy serverProxy = new ServerProxy();
        RegisterRequest request = new RegisterRequest("ben_1234", "layton", "ben@gmail.com",
                "Ben", "Layton", "m");

        String host = "localhost";
        String port = "8080";

        RegisterResult result = serverProxy.registerResult(request, host, port);

        assertNotNull(result);

    }

    @Test
    public void getFamilyTest() {

        ServerProxy serverProxy = new ServerProxy();
        String authToken = "99f300cf-e43c-405f-94d1-b97dda5cb8da";
        String host = "localhost";
        String port = "8080";

        PersonFamilyResult familyResult = serverProxy.getFamily(authToken, host, port);

        assertNotNull(familyResult);
        assertEquals(familyResult.getData().get(0).getFatherID(), "Blaine_McGary");
        assertEquals(familyResult.getData().get(0).getAssociatedUsername(), "sheila");
    }

    @Test
    public void getEventTest() {

        ServerProxy serverProxy = new ServerProxy();
        String authToken = "99f300cf-e43c-405f-94d1-b97dda5cb8da";
        String host = "localhost";
        String port = "8080";

        AllEventResult eventResult = serverProxy.event(authToken, host, port);

        assertNotNull(eventResult);
        assertEquals(eventResult.getData().get(0).getAssociatedUsername(), "sheila");
        assertEquals(eventResult.getData().get(0).getEventID(), "Sheila_Birth");

    }
}

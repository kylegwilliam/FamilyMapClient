package com.example.familymapclient;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import Request.AllEventRequest;
import Request.LoginRequest;
import Request.PersonFamilyRequest;
import Request.RegisterRequest;
import Request.SingleEventRequest;
import Request.SinglePersonRequest;
import Result.AllEventResult;
import Result.LoginResult;
import Result.PersonFamilyResult;
import Result.RegisterResult;
import Result.SingleEventResult;
import Result.SinglePersonResult;

public class ServerProxy {


    LoginResult login(LoginRequest request, String serverHost, String serverPort) { //POST


        Gson gson = new Gson();
        // This method shows how to send a POST request to a server

        try {
            // Create a URL indicating where the server is running, and which
            // web API operation we want to call
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");


            // Start constructing our HTTP request
            HttpURLConnection http = (HttpURLConnection)url.openConnection();


            // Specify that we are sending an HTTP POST request
            http.setRequestMethod("POST");

            // Indicate that this request will contain an HTTP request body
            http.setDoOutput(true);	// There is a request body


            // Add an auth token to the request in the HTTP "Authorization" header
            //http.addRequestProperty("Authorization", authToken);

            // Specify that we would like to receive the server's response in JSON
            // format by putting an HTTP "Accept" header on the request (this is not
            // necessary because our server only returns JSON responses, but it
            // provides one more example of how to add a header to an HTTP request).
            //http.addRequestProperty("Accept", "application/json");

            // Connect to the server and send the HTTP request
            http.connect();
            //String myString = http.getResponseMessage();
            // This is the JSON string we will send in the HTTP request body

            String reqData = gson.toJson(request);

            // Get the output stream containing the HTTP request body
            OutputStream reqBody = http.getOutputStream();

            // Write the JSON data to the request body
            writeString(reqData, reqBody);

            // Close the request body output stream, indicating that the
            // request is complete
            reqBody.close();


            // By the time we get here, the HTTP response has been received from the server.
            // Check to make sure that the HTTP response from the server contains a 200
            // status code, which means "success".  Treat anything else as a failure.
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {

                // The HTTP response status code indicates success,
                // so print a success message
                System.out.println("Route successfully claimed.");

                InputStream reader = http.getInputStream();
                String stringRead = readString(reader);
                LoginResult result = gson.fromJson(stringRead, LoginResult.class);
                reader.close();

                return result;

            }
            else {

                // The HTTP response status code indicates an error
                // occurred, so print out the message from the HTTP response
                System.out.println("ERROR: " + http.getResponseMessage());

                // Get the error stream containing the HTTP response body (if any)
                InputStream respBody = http.getErrorStream();

                // Extract data from the HTTP response body
                String respData = readString(respBody);

                // Display the data returned from the server
                System.out.println(respData);

                LoginResult result = gson.fromJson(respData, LoginResult.class);
                respBody.close();

                return result;
            }
        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
        }

        return null;

    }

    RegisterResult registerResult (RegisterRequest request, String serverHost, String serverPort) { // POST

        Gson gson = new Gson();
        // This method shows how to send a POST request to a server

        try {
            // Create a URL indicating where the server is running, and which
            // web API operation we want to call
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");

            // Start constructing our HTTP request
            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            // Specify that we are sending an HTTP POST request
            http.setRequestMethod("POST");

            // Indicate that this request will contain an HTTP request body
            http.setDoOutput(true);	// There is a request body


            // Add an auth token to the request in the HTTP "Authorization" header
            //http.addRequestProperty("Authorization", "afj232hj2332");

            // Specify that we would like to receive the server's response in JSON
            // format by putting an HTTP "Accept" header on the request (this is not
            // necessary because our server only returns JSON responses, but it
            // provides one more example of how to add a header to an HTTP request).
            //http.addRequestProperty("Accept", "application/json");

            // Connect to the server and send the HTTP request
            http.connect();

            // This is the JSON string we will send in the HTTP request body
            //change the request to a sting using gson

            String reqData = gson.toJson(request);

            // Get the output stream containing the HTTP request body
            OutputStream reqBody = http.getOutputStream();

            // Write the JSON data to the request body
            writeString(reqData, reqBody);

            // Close the request body output stream, indicating that the
            // request is complete
            reqBody.close();

            // By the time we get here, the HTTP response has been received from the server.
            // Check to make sure that the HTTP response from the server contains a 200
            // status code, which means "success".  Treat anything else as a failure.
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {

                // The HTTP response status code indicates success,
                // so print a success message
                System.out.println("Route successfully claimed.");

                InputStream reader = http.getInputStream();
                String stringRead = readString(reader);
                RegisterResult result = gson.fromJson(stringRead, RegisterResult.class);
                reader.close();

                return result;


            }
            else {

                // The HTTP response status code indicates an error
                // occurred, so print out the message from the HTTP response
                System.out.println("ERROR: " + http.getResponseMessage());

                // Get the error stream containing the HTTP response body (if any)
                InputStream respBody = http.getErrorStream();

                // Extract data from the HTTP response body
                String respData = readString(respBody);

                // Display the data returned from the server
                System.out.println(respData);

                RegisterResult result = gson.fromJson(respData, RegisterResult.class);
                respBody.close();

                return result;
            }
        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
        }

        return null;
    }

    PersonFamilyResult getFamily(String authToken, String serverHost, String serverPort) { //GET

        Gson gson = new Gson();
        // This method shows how to send a GET request to a server

        try {
            // Create a URL indicating where the server is running, and which
            // web API operation we want to call
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person");

            // Start constructing our HTTP request
            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            // Specify that we are sending an HTTP GET request
            http.setRequestMethod("GET");

            //in the video he said GET requests shouldn't contain HTTP request body

            // Indicate that this request will contain an HTTP request body
            http.setDoOutput(false);


            // Add an auth token to the request in the HTTP "Authorization" header
            http.addRequestProperty("Authorization", authToken);

            // Specify that we would like to receive the server's response in JSON
            // format by putting an HTTP "Accept" header on the request (this is not
            // necessary because our server only returns JSON responses, but it
            // provides one more example of how to add a header to an HTTP request).
            http.addRequestProperty("Accept", "application/json");


            // Connect to the server and send the HTTP request
            http.connect();

            // By the time we get here, the HTTP response has been received from the server.
            // Check to make sure that the HTTP response from the server contains a 200
            // status code, which means "success".  Treat anything else as a failure.
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {

                // Get the input stream containing the HTTP response body
                InputStream reader = http.getInputStream();

                // Extract JSON data from the HTTP response body
                String respData = readString(reader);

                // Display the JSON data returned from the server
                System.out.println(respData);

                //InputStream reader = http.getInputStream();
                //String stringRead = readString(reader);
                PersonFamilyResult result = gson.fromJson(respData, PersonFamilyResult.class);
                reader.close();

                return result;
            }
            else {
                // The HTTP response status code indicates an error
                // occurred, so print out the message from the HTTP response
                System.out.println("ERROR: " + http.getResponseMessage());

                // Get the error stream containing the HTTP response body (if any)
                InputStream respBody = http.getErrorStream();

                // Extract data from the HTTP response body
                String respData = readString(respBody);

                // Display the data returned from the server
                System.out.println(respData);

                PersonFamilyResult result = gson.fromJson(respData, PersonFamilyResult.class);
                respBody.close();

                return result;
            }
        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
        }
        return null;
    }

    AllEventResult event(String authToken, String serverHost, String serverPort) { //GET

        Gson gson = new Gson();
        // This method shows how to send a GET request to a server

        try {
            // Create a URL indicating where the server is running, and which
            // web API operation we want to call
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/event");

            // Start constructing our HTTP request
            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            // Specify that we are sending an HTTP GET request
            http.setRequestMethod("GET");

            //in the video he said GET requests shouldn't contain HTTP request body

            // Indicate that this request will contain an HTTP request body
            http.setDoOutput(false);


            // Add an auth token to the request in the HTTP "Authorization" header
            http.addRequestProperty("Authorization", authToken);

            // Specify that we would like to receive the server's response in JSON
            // format by putting an HTTP "Accept" header on the request (this is not
            // necessary because our server only returns JSON responses, but it
            // provides one more example of how to add a header to an HTTP request).
            http.addRequestProperty("Accept", "application/json");


            // Connect to the server and send the HTTP request
            http.connect();

            // By the time we get here, the HTTP response has been received from the server.
            // Check to make sure that the HTTP response from the server contains a 200
            // status code, which means "success".  Treat anything else as a failure.
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {

                // Get the input stream containing the HTTP response body
                InputStream respBody = http.getInputStream();

                // Extract JSON data from the HTTP response body
                String respData = readString(respBody);

                // Display the JSON data returned from the server
                System.out.println(respData);

                AllEventResult result = gson.fromJson(respData, AllEventResult.class);
                respBody.close();

                return result;

            }
            else {
                // The HTTP response status code indicates an error
                // occurred, so print out the message from the HTTP response
                System.out.println("ERROR: " + http.getResponseMessage());

                // Get the error stream containing the HTTP response body (if any)
                InputStream respBody = http.getErrorStream();

                // Extract data from the HTTP response body
                String respData = readString(respBody);

                // Display the data returned from the server
                System.out.println(respData);

                AllEventResult result = gson.fromJson(respData, AllEventResult.class);
                respBody.close();

                return result;
            }
        }
        catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
        }

        return null;
    }


    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    /*
        The writeString method shows how to write a String to an OutputStream.
    */
    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}

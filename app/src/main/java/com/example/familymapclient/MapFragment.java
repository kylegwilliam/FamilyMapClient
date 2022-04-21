package com.example.familymapclient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.Contacts;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import model.Event;
import model.Person;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, GoogleMap.OnMarkerClickListener {

    public List<Event> myEvents;
    public List<Person> myPeople;

    private final String eventID;
    private final Boolean bool;

    private GoogleMap map;

    private String firstName;
    private String lastName;
    private String gender;
    private String personID;

    public Person personObj;


    public Person getPerson() {
        return personObj;
    }

    public void setPersonObj(Person person) {
        this.personObj = person;
    }

    private TextView eventName;

    public MapFragment(String eventID, boolean bool) {
        this.eventID = eventID;
        this.bool = bool;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, container, savedInstanceState);
        View view = layoutInflater.inflate(R.layout.fragment_map, container, false);

        setHasOptionsMenu(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        eventName = view.findViewById(R.id.mapTextView);

        //SharedPreferences sharedPreferences = requireContext().getSharedPreferences("lifeValue", Context.MODE_PRIVATE);



        return view;
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        setHasOptionsMenu(true);
//        super.onCreate(savedInstanceState);
//    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        if (!bool) {
            inflater.inflate(R.menu.nav_menu, menu);
            super.onCreateOptionsMenu(menu, inflater);
        }
        else {
            System.out.println("is it working?");
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_search) {
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
        }

        if (id == R.id.nav_settings) {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        map.setOnMapLoadedCallback(this);

        DataCache dataCache;
        dataCache = DataCache.getInstance();

        myPeople = dataCache.getPerson();
        myEvents = dataCache.getEvent();


        for (int i = 0; i < myEvents.size(); i++) {

            Event event = myEvents.get(i);

            float googleColor = dataCache.getColorMap(event.getEventType().toLowerCase(Locale.ROOT));

            Marker marker = map.addMarker(new MarkerOptions().
                    position(new LatLng(event.getLatitude(), event.getLongitude())).
                    icon(BitmapDescriptorFactory.defaultMarker(googleColor)));


            marker.setTag(event);
        }

        map.setOnMarkerClickListener(this);

        if (bool) {

            Event EventOBJ = null;
            for (int i = 0; i < myEvents.size(); i++) {
                if (myEvents.get(i).getEventID().equals(eventID)) {
                    EventOBJ = myEvents.get(i);
                }
            }

            Float Lat = EventOBJ.getLatitude();
            Float Lon = EventOBJ.getLongitude();

            LatLng location = new LatLng(Lat, Lon);

            CameraUpdate update = CameraUpdateFactory.newLatLng(location);

            map.moveCamera(update);

            String personID = EventOBJ.getPersonID();

            Person PersonOBJ = null;
            for (int i = 0; i < myPeople.size(); i++) {
                if (myPeople.get(i).getPersonID().equals(personID)) {
                    PersonOBJ = myPeople.get(i);
                }
            }

            eventName.setText(PersonOBJ.getFirstName() + " " + PersonOBJ.getLastName() + '\n' + EventOBJ.getEventType() + ": " +
                    EventOBJ.getCity() + ", " + EventOBJ.getCountry() + " (" + EventOBJ.getYear() + ")");


            Drawable myDrawable = getResources().getDrawable(R.drawable.boy_image);

            if (PersonOBJ.getGender().equals("m")) {
                eventName.setCompoundDrawablesWithIntrinsicBounds(myDrawable, null, null, null);

            }

            else {
                Drawable boyDrawable = getResources().getDrawable(R.drawable.girl_image);
                eventName.setCompoundDrawablesWithIntrinsicBounds(boyDrawable, null, null, null);
            }


            boolean spouseLineSwitch = dataCache.getSpouseSwitch();
            if (spouseLineSwitch) {

                spouseLines(EventOBJ);
            }

            boolean lifeStory = dataCache.lifeStoryLines;
            if (lifeStory) {

                lifeStoryActivity(EventOBJ);

            }

        }

    }


    @Override
    public void onMapLoaded() {
        // You probably don't need this callback. It occurs after onMapReady and I have seen
        // cases where you get an error when adding markers or otherwise interacting with the map in
        // onMapReady(...) because the map isn't really all the way ready. If you see that, just
        // move all code where you interact with the map (everything after
        // map.setOnMapLoadedCallback(...) above) to here.
    }

    @Override
    public boolean onMarkerClick(Marker marker) {



        String makerName = marker.getTitle();
        //Toast.makeText(getActivity(), "It clicked.", Toast.LENGTH_SHORT).show();

        Event event = (Event) marker.getTag();

        //Do this inside of on create.

        DataCache dataCache;
        dataCache = DataCache.getInstance();

        List<Person> myPeople = dataCache.getPerson();


        for (int i = 0; i < myPeople.size(); i++) {
            if (myPeople.get(i).getPersonID().equals(event.getPersonID())) {
                firstName = myPeople.get(i).getFirstName();
                lastName = myPeople.get(i).getLastName();
                gender = myPeople.get(i).getGender();
                personID = myPeople.get(i).getPersonID();

            }
        }


        Drawable myDrawable = getResources().getDrawable(R.drawable.girl_image);

        if (gender.equals("f")) {
            eventName.setCompoundDrawablesWithIntrinsicBounds(myDrawable, null, null, null);
        }

        else {
            Drawable boyDrawable = getResources().getDrawable(R.drawable.boy_image);
            eventName.setCompoundDrawablesWithIntrinsicBounds(boyDrawable, null, null, null);
        }


        if (gender.equals("f")) {

        }

        eventName.setText(firstName + " " + lastName + '\n' + event.getEventType() + ": " +
                event.getCity() + ", " + event.getCountry() + " (" + event.getYear() + ")");

        eventName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Clicked the event obj", Toast.LENGTH_SHORT).show();

//                Bundle bundle = new Bundle();
//                bundle.putString("firstName", firstName);
//                bundle.putString("lastName", lastName);
//                bundle.putString("gender", gender);
//                bundle.putString("ID", personID);




                Intent intent = new Intent(getActivity(), PersonActivity.class);
                intent.putExtra("firstName", firstName);
                intent.putExtra("lastName", lastName);
                intent.putExtra("gender", gender);
                intent.putExtra("ID", personID);

                startActivity(intent);



//                PersonFragment fragment = new PersonFragment();
//                fragment.setArguments(bundle);

//                FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.fragmentFrameLayout, fragment)
//                                        .addToBackStack(null).commit();
//                fragmentManager.beginTransaction().add(R.id.fragmentFrameLayout, new PersonFragment()).commit();
            }
        });


        return false;
    }

    void drawLine(Event startEvent, Event endEvent, int googleColor, float width) {

        //Create start and end points for the line
        LatLng startPoint = new LatLng(startEvent.getLatitude(), startEvent.getLongitude());
        LatLng endPoint = new LatLng(endEvent.getLatitude(), endEvent.getLongitude());

        //Add line to map by specifying its endpoints, color, and width
        PolylineOptions options = new PolylineOptions()
                .add(startPoint)
                .add(endPoint)
                .color(googleColor)
                .width(width);

        map.addPolyline(options);
    }

    public void spouseLines(Event event) {

        int googleColor = Color.BLUE;

        Person person = eventToPerson(event);

        if (person.getSpouseID() != null) {

            Event spouseEvent = null;
            String spouseID = person.getSpouseID();
            for (int i = 0; i < myEvents.size(); i++) {
                if (myEvents.get(i).getPersonID().equals(spouseID)) {
                    spouseEvent = myEvents.get(i);
                    break;
                }
            }

            drawLine(event, spouseEvent, googleColor, 10);

        }

    }

    public void lifeStoryActivity(Event event) {

        int googleColor = Color.RED;

        Person person = eventToPerson(event);

        List<Event> EventList = new ArrayList<>();
        for (int i = 0; i < myEvents.size(); i++) {
            if (myEvents.get(i).getPersonID().equals(person.getPersonID())) {
                EventList.add(myEvents.get(i));
            }
        }

        int size = EventList.size();
        ArrayList<Event> eventA = new ArrayList<>();
        ArrayList<Event> eventB = new ArrayList<>();

        for (int i = 0; i < EventList.size() - 1; i++) {
            eventA.add(EventList.get(i));
        }

        for (int i = 1; i < EventList.size(); i++) {
            eventB.add(EventList.get(i));
        }

        for (int i = 0; i < eventA.size(); i++) {
            drawLine(eventA.get(i), eventB.get(i), googleColor, 10);
        }



    }

    public Person eventToPerson(Event event) {

        Person person = null;

        for (int i = 0; i < myPeople.size(); i++) {
            if (myPeople.get(i).getPersonID().equals(event.getPersonID())) {
                person = myPeople.get(i);
            }
        }

        return person;

    }

    public Event personToEvent(Person person) {

        Event event = null;

        for (int i = 0; i < myEvents.size(); i++) {
            if (myEvents.get(i).getPersonID().equals(person.getPersonID())) {
                event = myEvents.get(i);
            }
        }

        return event;

    }


}


